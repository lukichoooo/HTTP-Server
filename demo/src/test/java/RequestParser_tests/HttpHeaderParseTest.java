package RequestParser_tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.example.java_http_server.http.HttpParser;
import com.example.java_http_server.http.HttpRequest;
import com.example.java_http_server.http.exception.HttpParsingException;

public class HttpHeaderParseTest {

    public InputStream generate_GET_test() throws IOException {
        byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(
                "C:\\Users\\khund\\Java HTTP server\\simple_http_server\\demo\\src\\test\\java\\RequestParser_tests\\TestCase_1.txt"));
        return new ByteArrayInputStream(bytes);
    }

    @Test
    public void test_parseHeaders() throws HttpParsingException, IOException {
        HttpParser httpParser = new HttpParser();
        HttpRequest request = httpParser.parseRequest(generate_GET_test());
    }
}
