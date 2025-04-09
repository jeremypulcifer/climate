package org.pulcifer.climate.controller;


import org.pulcifer.climate.model.CityMeta;
import org.pulcifer.climate.service.CityComparisonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController public class AnalysisController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CityComparisonService service;

//    @GetMapping("/compare")
//    public Double differenceMeasurementForHourlyTemperatures(@RequestParam("curr") Integer currSeedId, @RequestParam("comp") Integer compSeedId) {
//        return service.differenceMeasurementForHourlyTemperatures(currSeedId, compSeedId);
//    }
    @GetMapping("/rank_similar")
    public List<CityMeta> getRankedListOfSimilarClimates(@RequestParam("curr") Integer currSeedId, @RequestParam("count") Integer count) {
        return service.getRankedListOfSimilarClimates(currSeedId, count);
    }

//    @GetMapping("/conditions_count")
//    public Map<String, Integer> getConditionsCount() {
//        return service.getConditionsCount();
//    }
}
