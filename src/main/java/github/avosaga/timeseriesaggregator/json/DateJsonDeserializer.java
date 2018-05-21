package github.avosaga.timeseriesaggregator.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJsonDeserializer extends JsonDeserializer<Date> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Override
    public Date deserialize(JsonParser parameter, DeserializationContext ctxt) throws IOException {
        Date date;

        try {
            date = DATE_FORMAT.parse(parameter.getText());
        } catch (ParseException e) {
            date = new Date();
        }

        return date;
    }
}
