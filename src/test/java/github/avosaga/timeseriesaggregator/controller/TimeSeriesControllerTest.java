package github.avosaga.timeseriesaggregator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.avosaga.timeseriesaggregator.model.TimeSeries;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import github.avosaga.timeseriesaggregator.service.TimeSeriesService;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TimeSeriesController.class)
public class TimeSeriesControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TimeSeriesService timeSeriesService;

    @Test
    public void shouldReturnTimeSeriesMap() throws Exception {
        TimeSeries expectedTimeSeries =
            new TimeSeries(1L, "SENSOR 1", 1234, new Date(118, 0, 1, 0, 0, 0));
        Map<String, List<TimeSeries>> expectedMap = new TreeMap<>();
        expectedMap.put("2018", Arrays.asList(expectedTimeSeries));
        when(timeSeriesService.findTimeSeriesBySensorNameAndGroupByTime(anyString(), anyString())).thenReturn(expectedMap);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/timeseries")
                                                                      .contentType(MediaType.APPLICATION_FORM_URLENCODED);
        request.param("sensorName", "");
        request.param("groupBy", "");

        mvc.perform(request)
           .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasKey("2018")))
           .andExpect(jsonPath("$.2018", hasSize(1)))
           .andExpect(jsonPath("$.2018[0].id", is(expectedTimeSeries.getId().intValue())))
           .andExpect(jsonPath("$.2018[0].sensorName", is(expectedTimeSeries.getSensorName())))
           .andExpect(jsonPath("$.2018[0].sensorValue", is(expectedTimeSeries.getSensorValue())))
           .andExpect(jsonPath("$.2018[0].timestamp", is("2018-01-01 00:00")));

        verify(timeSeriesService, times(1)).findTimeSeriesBySensorNameAndGroupByTime(anyString(), anyString());
    }

    @Test
    public void shouldGetGroupBys() throws Exception {
        Set<String> groupBys = new HashSet<>();
        groupBys.add("1");
        groupBys.add("2");
        when(timeSeriesService.getGroupBys()).thenReturn(groupBys);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/groupBys")
                                                                      .contentType(MediaType.APPLICATION_JSON_UTF8);

        mvc.perform(request)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(groupBys.size())))
                .andExpect(jsonPath("$[0]", is("1")))
                .andExpect(jsonPath("$[1]", is("2")));

        verify(timeSeriesService, times(1)).getGroupBys();
    }

    @Test
    public void shouldGetIdFromNewTimeSeries() throws Exception {
        TimeSeries expectedTimeSeries =
                new TimeSeries(1L, "SENSOR 1", 1, new Date(118, 0, 1, 0, 0, 0));
        when(timeSeriesService.saveTimeSeries(anyObject())).thenReturn(expectedTimeSeries);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/timeseries")
                                                                      .contentType(MediaType.APPLICATION_JSON_UTF8)
                                                                      .content(objectMapper.writeValueAsString(expectedTimeSeries));

        mvc.perform(request)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedTimeSeries.getId().intValue())))
                .andExpect(jsonPath("$.sensorName", is(expectedTimeSeries.getSensorName())))
                .andExpect(jsonPath("$.sensorValue", is(expectedTimeSeries.getSensorValue())))
                .andExpect(jsonPath("$.timestamp", is("2018-01-01 00:00")));
    }
}
