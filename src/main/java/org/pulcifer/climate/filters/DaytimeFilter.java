package org.pulcifer.climate.filters;

import org.pulcifer.climate.model.CityWeather;
import org.pulcifer.climate.model.DayWeather;
import org.pulcifer.climate.model.HourWeather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaytimeFilter {

    private static final DateFormat hourFmt = new SimpleDateFormat("hh:mm:ss");
    private static final Date lowerBound;
    private static final Date upperBound;

    static {
        try {
            lowerBound = hourFmt.parse("08:00:00");
            upperBound = hourFmt.parse("21:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<HourWeather> getHours(CityWeather cityWeather) {
        List<HourWeather> hours = new ArrayList<>();
        for (DayWeather day : cityWeather.getDays()) {
            for (HourWeather hour : day.getHours()) {
                Date date;
                try {
                    date = new SimpleDateFormat("hh:mm:ss").parse(hour.getDatetime());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (date.before(lowerBound) || date.after(upperBound)) continue;
                hours.add(hour);
            }
        }
        return hours;
    }

}
