package github.avosaga.timeseriesaggregator.service;

import github.avosaga.timeseriesaggregator.model.TimeSeries;
import github.avosaga.timeseriesaggregator.repository.TimeSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeSeriesService {

    private static final Map<String, String> DATE_FORMATS = new HashMap<String, String>(){{
        put("hour" , "MM-dd-yyyy HH':00'");
        put("day"  , "MM-dd-yyyy");
        put("month", "MM-yyyy");
        put("year" , "yyyy");
    }};

    private TimeSeriesRepository timeSeriesRepository;

    @Autowired
    public TimeSeriesService(TimeSeriesRepository timeSeriesRepository) {
        this.timeSeriesRepository = timeSeriesRepository;
    }

    public Map<String, List<TimeSeries>> findTimeSeriesBySensorNameAndGroupByTime(String sensorName,
                                                                                  String groupBy) {
        if(sensorName == null) {
            sensorName = "";
        }

        List<TimeSeries> timeSeriesList =
            timeSeriesRepository.findBySensorNameContainingIgnoreCase(sensorName);

        DateFormat dateFormat = new SimpleDateFormat(
            groupBy != null && DATE_FORMATS.containsKey(groupBy) ? DATE_FORMATS.get(groupBy) : DATE_FORMATS.get("day")
        );

        return timeSeriesList.stream()
                             .collect(
                                 Collectors.groupingBy(timeSeries -> dateFormat.format(timeSeries.getTimestamp()),
                                                       TreeMap::new,
                                                       Collectors.toList())
                             );
    }

    public TimeSeries saveTimeSeries(TimeSeries timeSeries) {
        return timeSeriesRepository.save(timeSeries);
    }

    public Set<String> getGroupBys() {
        return DATE_FORMATS.keySet();
    }
}
