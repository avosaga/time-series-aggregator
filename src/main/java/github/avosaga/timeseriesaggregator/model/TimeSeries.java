package github.avosaga.timeseriesaggregator.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import github.avosaga.timeseriesaggregator.json.DateJsonDeserializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TIMESERIES")
public class TimeSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sensorName;
    private Integer sensorValue;
    @JsonDeserialize(using = DateJsonDeserializer.class)
    private Date timestamp;

    public TimeSeries() {}

    public TimeSeries(Long id, String sensorName, Integer sensorValue, Date timestamp) {
        this.id = id;
        this.sensorName = sensorName;
        this.sensorValue = sensorValue;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Integer getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Integer sensorValue) {
        this.sensorValue = sensorValue;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSeries)) return false;

        TimeSeries that = (TimeSeries) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
