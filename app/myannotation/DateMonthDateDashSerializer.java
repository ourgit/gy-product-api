package myannotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by win7 on 2016/8/13.
 */
public class DateMonthDateDashSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (null != value) {
            if (value == 0) jsonGenerator.writeString("");
            else jsonGenerator.writeString(formatToMDHMSBySecond(value));
        } else jsonGenerator.writeString("");
    }

    /**
     * 秒数转化为MM-DD HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public String formatToMDHMSBySecond(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        DateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

}
