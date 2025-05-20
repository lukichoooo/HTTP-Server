package com.example.java_http_server.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Json {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode parse(String json) throws JsonMappingException, JsonProcessingException {
        return mapper.readTree(json);
    }

    public static String stringify(Object obj) throws JsonProcessingException {
        ObjectWriter writer = mapper.writer();
        return writer.writeValueAsString(obj);
    }

    public static String stringifyPretty(Object obj) throws JsonProcessingException {
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonMappingException, JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}
