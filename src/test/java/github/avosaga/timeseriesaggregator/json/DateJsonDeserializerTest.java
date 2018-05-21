package github.avosaga.timeseriesaggregator.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DateJsonDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private DateJsonDeserializer dateJsonDeserializer = new DateJsonDeserializer();

    @Test
    public void shouldParseStringToDate() throws Exception {
        Date expectDate = new Date(118, 4, 21, 10, 0, 0);
        when(jsonParser.getText()).thenReturn("2018-05-21T10:00");

        Date actualDate = dateJsonDeserializer.deserialize(jsonParser, deserializationContext);

        assertThat(actualDate, notNullValue());
        assertThat(actualDate, is(expectDate));
        verify(jsonParser, times(1)).getText();
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void shouldGetCurrentDateIfExceptionThrown() throws Exception {
        Date expectedDate = new Date();
        when(jsonParser.getText()).thenReturn("NOT A DATE");

        Date actualDate = dateJsonDeserializer.deserialize(jsonParser, deserializationContext);

        assertThat(actualDate, notNullValue());
        assertThat(new Long(actualDate.getTime()).doubleValue(),
                   closeTo(new Long(expectedDate.getTime()).doubleValue(), 1000D));
        verify(jsonParser, times(1)).getText();
        verifyZeroInteractions(deserializationContext);
    }
}
