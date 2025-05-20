package json_test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.example.java_http_server.util.json.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonTest {

    private final String jsonTestCase = "{ \"name\": \"Chiaki\", \"age\": 17, \"isGamer\": true }";

    @Test
    public void parse() throws JsonMappingException, JsonProcessingException {
        assertEquals("Chiaki", Json.parse(jsonTestCase).get("name").asText());
    }

    @Test
    public void toJson() throws JsonProcessingException {
        POJO pojo = Json.fromJson(jsonTestCase, POJO.class);
        assertEquals("Chiaki", pojo.name);
        assertEquals(17, pojo.age);
        assertEquals(true, pojo.isGamer);
    }

}
