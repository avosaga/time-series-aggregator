package github.avosaga.timeseriesaggregator.controller;

import github.avosaga.timeseriesaggregator.model.TimeSeries;
import github.avosaga.timeseriesaggregator.service.TimeSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TimeSeriesController {

    private TimeSeriesService timeSeriesService;

    @Autowired
    public TimeSeriesController(TimeSeriesService timeSeriesService) {
        this.timeSeriesService = timeSeriesService;
    }

    @GetMapping("/api/groupBys")
    public ResponseEntity<Set<String>> getGroupBys() {
        return ResponseEntity.ok(timeSeriesService.getGroupBys());
    }

    @GetMapping("/api/timeseries")
    public ResponseEntity<Map<String, List<TimeSeries>>> getTimeSeries(@RequestParam(required = false) String groupBy,
                                                                       @RequestParam(required = false) String sensorName) {

        return ResponseEntity.ok(timeSeriesService.findTimeSeriesBySensorNameAndGroupByTime(sensorName, groupBy));
    }

    @PostMapping("/api/timeseries")
    public ResponseEntity<TimeSeries> saveTimeSeries(@RequestBody TimeSeries timeSeries) {
        return ResponseEntity.ok(timeSeriesService.saveTimeSeries(timeSeries));
    }
}
