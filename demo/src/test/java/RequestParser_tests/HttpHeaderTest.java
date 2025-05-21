package RequestParser_tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.example.java_http_server.http.HttpParser;
import com.example.java_http_server.http.HttpRequest;
import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpHeaderTest {

    HttpParser httpParser = new HttpParser();

    public static InputStream generate_GET_test() {
        String raw = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: TestClient/1.0\r\n" +
                "Connection: keep-alive\r\n" + // ← added this line
                "\r\n"; // end of headers

        return new ByteArrayInputStream(raw.getBytes(StandardCharsets.US_ASCII));
    }

    public static InputStream generate_invalidHeader_test() {
        String raw = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent:: TestClient/1.0\r\n" +
                "Connection: keep-alive\r\n" + // ← added this line
                "\r\n"; // end of headers

        return new ByteArrayInputStream(raw.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    public void test_parseHeaders() throws HttpParsingException, IOException {
        HttpRequest request = httpParser.parseRequest(generate_GET_test());
        assertNotNull(request);
        assertEquals("localhost:8080", request.getHttpHeaderValue("Host"));
        assertEquals("keep-alive", request.getHttpHeaderValue("Connection"));
        assertEquals("TestClient/1.0", request.getHttpHeaderValue("User-Agent"));
    }

    @Test
    public void test_invalidHeader() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_invalidHeader_test());
        } catch (HttpParsingException ex) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_BAD_REQUEST, ex.getErrorCode());
        }

    }

}
