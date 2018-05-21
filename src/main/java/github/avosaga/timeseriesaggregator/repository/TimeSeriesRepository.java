package github.avosaga.timeseriesaggregator.repository;

import github.avosaga.timeseriesaggregator.model.TimeSeries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSeriesRepository extends JpaRepository<TimeSeries, Long> {

    List<TimeSeries> findBySensorNameContainingIgnoreCase(String sensorName);
}
