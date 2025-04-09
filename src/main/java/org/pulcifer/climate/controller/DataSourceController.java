package org.pulcifer.climate.controller;


import org.pulcifer.climate.model.CityMeta;
import org.pulcifer.climate.model.CityWeather;
import org.pulcifer.climate.service.DataSourceService;
import org.pulcifer.climate.service.MetroFilteringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController public class DataSourceController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private MetroFilteringService metroFilteringService;

//    @Autowired
//    public void setDataSourceService(DataSourceService dataSourceService) {
//        this.dataSourceService = dataSourceService;
//    }

    @GetMapping("/load_weather")
    public List<CityWeather> loadWeatherData() {
        return dataSourceService.loadWeatherData();
    }

    @GetMapping("/filter_metros")
    public List<CityMeta> filterMetros() {
        return metroFilteringService.identifyMetroAreas();
    }
}
