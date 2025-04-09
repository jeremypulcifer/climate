package org.pulcifer.climate.service;

import org.pulcifer.climate.model.CityMeta;
import org.pulcifer.climate.model.CityWeather;
import org.pulcifer.climate.model.DayWeather;
import org.pulcifer.climate.model.HourWeather;
import org.pulcifer.climate.repo.CityMetaRepository;
import org.pulcifer.climate.repo.CityWeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.pulcifer.climate.filters.DaytimeFilter.getHours;

@Service
public class CityComparisonService {

    private final Logger logger = LoggerFactory.getLogger(CityComparisonService.class);
    private CityWeatherRepository repo;
    private CityMetaRepository metaRepo;

    @Autowired
    public void setRepo(CityWeatherRepository repo) {
        this.repo = repo;
    }

    @Autowired
    public void setMetaRepo(CityMetaRepository metaRepo) {
        this.metaRepo = metaRepo;
    }

    public List<CityMeta> getRankedListOfSimilarClimates(Integer currSeedId, Integer count) {
        long getScoresStart = new Date().getTime();
        List<CityMeta> metas = setScores(currSeedId);
        logger.info("Set Scores executed in {} ms.", new Date().getTime() - getScoresStart);

        long setRanksStart = new Date().getTime();
        setRanks(metas);
        logger.info("Set Ranks executed in {} ms.", new Date().getTime() - setRanksStart);

        long setTotalScoresStart = new Date().getTime();
        setTotalScores(metas);
        logger.info("Set Total Scores executed in {} ms.", new Date().getTime() - setTotalScoresStart);

        long setTotalRanksStart = new Date().getTime();
        setTotalRanks(metas);
        logger.info("Set Total Ranks executed in {} ms.", new Date().getTime() - setTotalRanksStart);

        return truncateList(metas, count);
    }

    private List<CityMeta> setScores(Integer currSeedId) {
        int i = 1;
        List<CityMeta> metas = metaRepo.findAll();
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            for (CityMeta cityMeta : metas) {
                Integer cityId = cityMeta.getCityId();
                if (cityId == null || cityId.equals(currSeedId)) continue;
                Thread t = new Thread(() -> setScores(cityMeta, getCityWeatherById(currSeedId), getCityWeatherById(cityId)));
                executor.execute(t);
            }
            awaitTerminationAfterShutdown(executor);
        }
        return metas;
    }

    void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(120, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void setScores(CityMeta cityMeta, CityWeather curr, CityWeather comp) {
        cityMeta.getScores().put(CityMeta.ScoreCategories.TEMPERATURE, new CityMeta.ScoreAndRank(curveAnalysisForTemperature(curr, comp)));
        cityMeta.getScores().put(CityMeta.ScoreCategories.HUMIDITY, new CityMeta.ScoreAndRank(curveAnalysisForHumidity(curr, comp)));
        cityMeta.getScores().put(CityMeta.ScoreCategories.CLOUD_COVER, new CityMeta.ScoreAndRank(curveAnalysisForCloudCover(curr, comp)));
        cityMeta.getScores().put(CityMeta.ScoreCategories.RAIN, new CityMeta.ScoreAndRank(curveAnalysisForRain(curr, comp)));
    }

    private void setRanks(List<CityMeta> metas) {
        setRankForScores(metas, CityMeta.ScoreCategories.TEMPERATURE);
        setRankForScores(metas, CityMeta.ScoreCategories.HUMIDITY);
        setRankForScores(metas, CityMeta.ScoreCategories.CLOUD_COVER);
        setRankForScores(metas, CityMeta.ScoreCategories.RAIN);
    }

    private void setTotalScores(List<CityMeta> metas) {
        for (CityMeta cityMeta : metas) {
            BigDecimal rankSum = BigDecimal.ONE;
            for (Map.Entry<CityMeta.ScoreCategories, CityMeta.ScoreAndRank> scoreAndRank : cityMeta.getScores().entrySet()) {
                if (CityMeta.ScoreCategories.TOTAL == scoreAndRank.getKey()) continue;
                rankSum = rankSum.multiply(BigDecimal.valueOf(scoreAndRank.getValue().rank).sqrt(MathContext.DECIMAL32));
            }
            BigDecimal rankTotal = rankSum.divide(BigDecimal.valueOf(cityMeta.getScores().size()), MathContext.DECIMAL32);
            CityMeta.ScoreAndRank totalScore = new CityMeta.ScoreAndRank(rankTotal);
            cityMeta.getScores().put(CityMeta.ScoreCategories.TOTAL, totalScore);
        }
    }

    private List<CityMeta> truncateList(List<CityMeta> metas, Integer count) {
        if (count != null && metas.size() > count) return metas.subList(0, count);
        return metas;
    }

    private void setTotalRanks(List<CityMeta> metas) {
        setRankForScores(metas, CityMeta.ScoreCategories.TOTAL);
    }

    private void setRankForScores(List<CityMeta> metas, CityMeta.ScoreCategories category) {
        metas.removeIf(meta -> meta.getScores() == null || meta.getScores().get(category) == null);
        metas.sort(Comparator.comparing(s -> s.getScores().get(category).score));
        for (int i = 1; i <= metas.size(); i++) {
            metas.get(i - 1).getScores().get(category).rank = i;
        }
    }

    private CityWeather getCityWeatherById(Integer id) {
        return repo.findBySeedId(id);
    }

    private BigDecimal curveAnalysisForTemperature(CityWeather curr, CityWeather comp) {
        return curveAnalysis(curr, comp, (this::getTemperatureCurve));
    }

    private BigDecimal curveAnalysisForHumidity(CityWeather curr, CityWeather comp) {
        return curveAnalysis(curr, comp, (this::getHumidityCurve));
    }

    private BigDecimal curveAnalysisForCloudCover(CityWeather curr, CityWeather comp) {
        return curveAnalysis(curr, comp, (this::getCloudCoverCurve));
    }

    private BigDecimal curveAnalysisForRain(CityWeather curr, CityWeather comp) {
        return curveAnalysis(curr, comp, (this::getRainCurve));
    }

    private BigDecimal curveAnalysis(CityWeather curr, CityWeather comp, CurveCalculator curveCalculator) {
        if (curr == null || comp == null) return BigDecimal.ONE;
        List<BigDecimal> currCurveImmutable = curveCalculator.calculator(curr);
        List<BigDecimal> compCurveImmutable = curveCalculator.calculator(comp);

        if (currCurveImmutable.isEmpty() || compCurveImmutable.isEmpty()) return BigDecimal.ONE;

        List<BigDecimal> currCurve = new ArrayList<>(currCurveImmutable);
        List<BigDecimal> compCurve = new ArrayList<>(compCurveImmutable);

        currCurve.sort(BigDecimal::compareTo);
        compCurve.sort(BigDecimal::compareTo);

        if (currCurve.size() != compCurve.size()) {
            int currSize = currCurve.size();
            int compSize = compCurve.size();

            if (currSize < compSize) compCurve = compCurve.subList(0, currSize - 1);
            else currCurve = currCurve.subList(0, compSize - 1);
        }

        return differenceMeasure(currCurve, compCurve);
    }

    private BigDecimal differenceMeasure(List<BigDecimal> curr, List<BigDecimal> comp) {
        BigDecimal totalDistance = getTotalDistance(curr, comp);
        return totalDistance.divide(BigDecimal.valueOf(curr.size()), MathContext.DECIMAL32);
    }

    private BigDecimal getTotalDistance(List<BigDecimal> curve1, List<BigDecimal> curve2) {
        BigDecimal totalDistance = BigDecimal.valueOf(0);
        for (int i = 0; i < curve1.size() && i < curve2.size(); i++)
            totalDistance = totalDistance.add(curve1.get(i).subtract(curve2.get(i)).abs());
        return totalDistance;
    }

    private List<BigDecimal> getTemperatureCurve(CityWeather cityWeather) {
        return getHours(cityWeather).stream().map(HourWeather::getTemp).toList();
    }

    private List<BigDecimal> getHumidityCurve(CityWeather cityWeather) {
        return getHours(cityWeather).stream().map(HourWeather::getHumidity).toList();
    }

    private List<BigDecimal> getCloudCoverCurve(CityWeather cityWeather) {
        return getHours(cityWeather).stream().map(HourWeather::getCloudcover).toList();
    }

    private List<BigDecimal> getRainCurve(CityWeather cityWeather) {
        return getDays(cityWeather).stream().map(DayWeather::getPrecipcover).map(BigDecimal::valueOf).toList();
    }

    private List<DayWeather> getDays(CityWeather cityWeather) {
        return cityWeather.getDays();
    }
    interface CurveCalculator {
        List<BigDecimal> calculator(CityWeather weather);
    }
}
