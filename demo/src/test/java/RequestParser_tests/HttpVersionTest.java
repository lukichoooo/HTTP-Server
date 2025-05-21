package RequestParser_tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.example.java_http_server.http.HttpParser;
import com.example.java_http_server.http.HttpRequest;
import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpVersionTest {

    HttpParser httpParser = new HttpParser();

    private InputStream generate_invalid_version_test() {
        String rawRequest = "GET / HTTP/\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        return new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generate_unsupported_version_test() {
        String rawRequest = "GET / HTTP/1.2\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        return new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generate_supported_old_version_test() {
        String rawRequest = "GET / HTTP/1.0\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n"; // CRLF to indicate end of headers

        return new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    public void test_invalid_version() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_invalid_version_test());
            fail("Expected HttpParsingException was not thrown");
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_HTTP_VERSION_NOT_SUPPORTED, e.getErrorCode());
        }
    }

    @Test
    public void test_unsupported_version() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_unsupported_version_test());
            fail("Expected HttpParsingException was not thrown");
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_HTTP_VERSION_NOT_SUPPORTED, e.getErrorCode());
        }
    }

    @Test
    public void test_supported_old_version() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_supported_old_version_test());
            assertEquals(request.getRequestedVersion(), "HTTP/1.0");
            assertEquals(request.bestCompatableVersion().LITERAL, "HTTP/1.1");
        } catch (HttpParsingException e) {
            fail("Unexpected HttpParsingException");
        }
    }
}
