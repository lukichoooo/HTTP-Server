package RequestParser_tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.example.java_http_server.http.HttpMethod;
import com.example.java_http_server.http.HttpParser;
import com.example.java_http_server.http.HttpRequest;
import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpParserTest {

    final String GET_requestPath = "C:\\Users\\khund\\Java HTTP server\\simple_http_server\\demo\\src\\test\\java\\RequestParser_tests\\TestCase_1.txt";
    HttpParser httpParser = new HttpParser();

    private InputStream generate_GET_test() throws IOException {
        byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(GET_requestPath));
        return new ByteArrayInputStream(bytes);
    }

    private InputStream generate_Bad_test() throws IOException {
        String httpRequest = "GYAAAT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: \r\n" + // Assuming there was a value for Connection and it's intentionally blank or to
                                     // be filled
                "Accept-Language: en-US,en;q=0.9\r\n";
        return new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generate_invalid_method() throws IOException {
        String httpRequest = "GYAAAT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: \r\n" + // Assuming there was a value for Connection and it's intentionally blank or to
                                     // be filled
                "Accept-Language: en-US,en;q=0.9\r\n";
        return new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generate_method_long() throws IOException {
        String httpRequest = "GYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAATGYAAAT / HTTP/1.1\r\n"
                +
                "Host: localhost:8080\r\n" +
                "Connection: \r\n" + // Note: There's a space after "Connection:" in your input before the newline
                "Accept-Language: en-US,en;q=0.9\r\n";
        return new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generate_empty_req() throws IOException {
        byte[] bytes = ("\r\n" + //
                "Accept-Language:\r\n" + //
                "en-US,en;q=0.9").getBytes();
        return new ByteArrayInputStream(bytes);
    }

    private InputStream generate_no_LF() {
        String rawRequest = "GET / HTTP/1.1\r" + // Only CR, no LF
                "Host: localhost:8080\r" +
                "Accept-Language: en-US,en;q=0.9\r" +
                "\r"; // Final CR-only ending

        return new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generate_invalid_req_target_test() {
        String rawRequest = "GET  HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n"; // End of headers

        return new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.US_ASCII));
    }

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
    public void test() {
        try {
            InputStream requestStream = generate_GET_test();
            HttpRequest request = httpParser.parseRequest(requestStream);

            assertNotNull(request);
            assertEquals(HttpMethod.Method.GET, request.getMethod());
            assertEquals("/", request.getRequestTarget());

        } catch (IOException | HttpParsingException e) {
            fail("Exception during parsing: " + e.getMessage());
        }
    }

    @Test
    public void testBadRequest() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_Bad_test());
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_METHOD_NOT_ALLOWED, e.getErrorCode());
        }
    }

    @Test
    public void testBadRequest_LongMethod() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_invalid_method());
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_METHOD_NOT_ALLOWED, e.getErrorCode());
        }
    }

    @Test
    public void testBadRequest_tooManyItems() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_method_long());
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_METHOD_NOT_ALLOWED, e.getErrorCode());
        }
    }

    @Test
    public void test_EmptyRequest() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_empty_req());
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_NO_CONTENT, e.getErrorCode());
        }
    }

    @Test
    public void test_no_LF() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_no_LF());
            fail("Expected HttpParsingException was not thrown");
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    public void test_invalid_req_target() throws IOException {
        try {
            HttpRequest request = httpParser.parseRequest(generate_invalid_req_target_test());
            fail("Expected HttpParsingException was not thrown");
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_INTERNAL_SERVER_ERROR, e.getErrorCode());
        }
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
