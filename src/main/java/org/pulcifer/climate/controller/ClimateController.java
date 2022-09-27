package org.pulcifer.climate.controller;

import org.pulcifer.climate.dto.SimilarCity;
import org.pulcifer.climate.model.City;
import org.pulcifer.climate.svc.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ClimateController {

    @Autowired
    CityService svc;

    @GetMapping("/city/{id}")
    public City city(@PathVariable Integer id) {
        return svc.getCityById(id);
    }

    @GetMapping("/citiesContaining")
    public Map<Integer, String> citiesContaining(@RequestParam(value = "name") String name) {
       return svc.citiesContaining(name).stream()
                .collect(Collectors.toMap(City::getCityId, this::formatCityCountry));
    }

    private String formatCityCountry(City city) {
        return String.format("%s/%s", city.getCityName(), city.getCountry());
    }

    @GetMapping("/similar")
    public List<SimilarCity> similar(@RequestParam(value = "cityId")Integer cityId, @RequestParam(value = "min_pop")Integer minPop, @RequestParam(value = "max_pop")Integer maxPop) {
        return svc.retrieveSimilarCities(cityId, minPop, maxPop);
    }


    @RequestMapping( method = RequestMethod.GET,
            value = "/similarCSV")
    public void similarCSV(@RequestParam(value = "cityId")Integer cityId, @RequestParam(value = "min_pop")Integer minPop, @RequestParam(value = "max_pop")Integer maxPop, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition","attachment;filename=myFile.txt");
        ServletOutputStream out = response.getOutputStream();
        out.println("county,city,id,highestavgtemp,lowestavgtemp,avgtemp,lowesttemp,highesttemp,raindays,rainfall,diff");

        List<SimilarCity> cities = svc.retrieveSimilarCities(cityId, minPop, maxPop);
        for (SimilarCity city : cities) {
            out.println(cityDataAsCSV(city));
        }
        out.flush();
        out.close();
    }

    private String cityDataAsCSV(SimilarCity c) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                c.getCountry().replaceAll(",","-"), c.getCityName().replaceAll(",","-"),
                c.getPopulation(),
                c.getCityId(),
                c.getHighestAvgTemperature(),
                c.getLowestAvgTemperature(),
                c.getAvgTemperature(),
                c.getLowestTemperature(),
                c.getHighestTemperature(),
                c.getRainDays(), c.getRainfall(), c.getDiff());
    }
}
