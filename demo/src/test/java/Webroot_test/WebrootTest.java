package Webroot_test;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.java_http_server.connection.io.WebrootController;
import com.example.java_http_server.connection.io.WebrootException.WebrootNotFound;

public class WebrootTest {

    String configPath = "C:\\Users\\khund\\Java HTTP server\\simple_http_server\\demo\\src\\main\\resources\\config.json";
    static WebrootController webrootController;
    private static Method pathEndsWithSlash;
    private static Method relativePathExists;

    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            String webrootPath = "src/main/java/com/example/java_http_server/WebRoot";
            webrootController = new WebrootController(webrootPath);
            pathEndsWithSlash = WebrootController.class.getDeclaredMethod("pathEndsWithSlash",
                    String.class);
            relativePathExists = WebrootController.class.getDeclaredMethod("relativePathExists",
                    String.class);
            pathEndsWithSlash.setAccessible(true);
            relativePathExists.setAccessible(true);
        } catch (WebrootNotFound e) {
            e.printStackTrace();
            fail("WebrootNotFound during setup");
        }
    }

    @Test
    public void badPath_test() {
        try {
            WebrootController webrootController = new WebrootController(
                    "C:\\Users\\khund\\Java HTTP server\\simple_http_server\\demo\\src\\main\\java\\com\\example\\java_http_server\\WebRoot2");
            fail();
        } catch (WebrootNotFound ex) {
        }
    }

    @Test
    public void validRelativePath_test() {
        try {
            String relativePath = "src/main/java/com/example/java_http_server/WebRoot";
            WebrootController webrootController = new WebrootController(relativePath);
        } catch (WebrootNotFound ex) {
            fail();
        }
    }

    @Test
    public void badRelativePath_test() {
        try {
            String relativePath = "src/main/java/com/example/java_http_server/WebRoot2";
            WebrootController webrootController = new WebrootController(relativePath);
            fail();
        } catch (WebrootNotFound ex) {
        }
    }

    @Test
    public void test_CheckIfPathEndsWithSlash_good() throws Exception {
        String input = "folder/path/";
        boolean result = (boolean) pathEndsWithSlash.invoke(webrootController, input);
        assertEquals(true, result);
    }

    @Test
    public void test_CheckIfPathEndsWithSlash_bad() throws Exception {
        String input = "folder/path";
        boolean result = (boolean) pathEndsWithSlash.invoke(webrootController, input);
        assertEquals(false, result);
    }

    @Test
    public void test_checkIfRelativePathExists_good() throws Exception {
        String input = "/index.html";
        boolean result = (boolean) relativePathExists.invoke(webrootController, input);
        assertEquals(true, result);
    }

    @Test
    public void test_checkIfRelativePathExists_bad() throws Exception {
        String input = "/FrankHorgan.html";
        boolean result = (boolean) relativePathExists.invoke(webrootController, input);
        assertEquals(false, result);
    }

    @Test
    public void test_getMimeType_good() throws Exception {
        String result = webrootController.getMimeType("/");
        assertEquals("text/html", result);
    }

    @Test
    public void test_getMimeType_bad() throws Exception {
        try {
            String result = webrootController.getMimeType("/favicon.ico");
            fail();
        } catch (FileNotFoundException e) {
        }
    }

    // @Test
    // public void test_readFile() throws Exception {
    //     try {
    //         byte[] result = webrootController.readFile("/index.html");
    //         InputStream expected = getClass().getResourceAsStream("/index.html");
    //         assertEquals(expected, result);
    //     } catch (FileNotFoundException e) {
    //     }
    // }

}
