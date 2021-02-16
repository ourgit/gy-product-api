package myannotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import utils.ValidationUtil;

import java.io.IOException;

/**
 * Created by win7 on 2016/8/13.
 */
public class EscapeSimpleHtmlSerializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken currentToken = jp.getCurrentToken();
        if (null == currentToken) return "";
        if (currentToken == JsonToken.VALUE_STRING) {
            return escapeHtml(jp.getText().trim());
        }
        return "";
    }

    /**
     * 转义html脚本
     *
     * @param value
     * @return
     */
    public String escapeHtml(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "（").replaceAll("\\)", "）");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("select", "");
        value = value.replaceAll("insert", "");
        value = value.replaceAll("update", "");
        value = value.replaceAll("delete", "");
        value = value.replaceAll("%", "\\%");
        value = value.replaceAll("union", "");
        value = value.replaceAll("load_file", "");
        value = value.replaceAll("outfile", "");
        return value;
    }
}
