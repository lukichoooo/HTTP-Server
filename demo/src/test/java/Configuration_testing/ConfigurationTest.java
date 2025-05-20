package Configuration_testing;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.example.java_http_server.configuration.ConfigurationManager;

public class ConfigurationTest {

    String filePath = "C:\\Users\\khund\\Java HTTP server\\simple_http_server\\demo\\src\\test\\java\\Configuration_testing\\testCase_1.json";

    @Test
    public void test() {
        ConfigurationManager.loadConfiguration(filePath);
        assertEquals("your valid webroot", ConfigurationManager.getInstance().getCurrentConfiguration().getWebroot());
    }
}
