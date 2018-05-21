package github.avosaga.timeseriesaggregator.service;

import github.avosaga.timeseriesaggregator.model.TimeSeries;
import github.avosaga.timeseriesaggregator.repository.TimeSeriesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeSeriesServiceTest {

    @Mock
    private TimeSeriesRepository timeSeriesRepository;

    @InjectMocks
    private TimeSeriesService timeSeriesService;

    @Before
    public void setup() {
        when(timeSeriesRepository.findBySensorNameContainingIgnoreCase(anyString())).thenReturn(createMockData());
    }

    @Test
    public void shouldGetGroupBys() {
        Set<String> groupBys = timeSeriesService.getGroupBys();

        assertThat(groupBys, not(empty()));
        assertThat(groupBys, containsInAnyOrder("hour", "day", "month", "year"));
    }

    @Test
    public void shouldGroupAllTimeSeriesByHour() {
        assertGroupedTimeSeries("hour",
            "02-01-2018 01:00",
            "02-02-2018 04:00",
            "03-06-2018 06:00",
            "03-07-2018 07:00",
            "09-08-2017 08:00",
            "10-09-2017 09:00"
        );
    }

    @Test
    public void shouldGroupTimeSeriesByDay() {
        assertGroupedTimeSeries("day",
            "02-01-2018",
            "02-02-2018",
            "03-06-2018",
            "03-07-2018",
            "09-08-2017",
            "10-09-2017"
        );
    }

    @Test
    public void shouldGroupTimeSeriesByMonth() {
        assertGroupedTimeSeries("month", "02-2018", "03-2018", "09-2017", "10-2017");
    }

    @Test
    public void shouldGroupTimeSeriesByYear() {
        assertGroupedTimeSeries("year", "2018", "2017");
    }

    private void assertGroupedTimeSeries(String groupBy, String ...expectedKeys) {
        Map<String, List<TimeSeries>> groupedByHour =
                timeSeriesService.findTimeSeriesBySensorNameAndGroupByTime("", groupBy);

        assertThat(groupedByHour, notNullValue());
        assertThat(groupedByHour.keySet().size(), is(expectedKeys.length));
        assertThat(groupedByHour.keySet(), containsInAnyOrder(expectedKeys));
        verify(timeSeriesRepository, times(1)).findBySensorNameContainingIgnoreCase(anyString());
    }

    private static List<TimeSeries> createMockData() {
        return Arrays.asList(
            new TimeSeries(1L, "SENSOR 1", 1, new Date(118, 1, 1, 1, 1, 1)),
            new TimeSeries(2L, "SENSOR 2", 2, new Date(118, 1, 1, 1, 2, 1)),
            new TimeSeries(3L, "SENSOR 3", 3, new Date(118, 1, 1, 1, 3, 3)),
            new TimeSeries(4L, "SENSOR 4", 4, new Date(118, 1, 2, 4, 1, 1)),
            new TimeSeries(5L, "SENSOR 5", 5, new Date(118, 1, 2, 4, 1, 1)),
            new TimeSeries(6L, "SENSOR 6", 6, new Date(118, 2, 6, 6, 1, 1)),
            new TimeSeries(7L, "SENSOR 7", 7, new Date(118, 2, 7, 7, 1, 1)),
            new TimeSeries(8L, "SENSOR 8", 8, new Date(117, 8, 8, 8, 1, 1)),
            new TimeSeries(9L, "SENSOR 9", 8, new Date(117, 9, 9, 9, 1, 1))
        );
    }
}
