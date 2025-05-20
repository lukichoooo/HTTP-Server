package com.example.java_http_server.http;

import java.net.http.HttpHeaders;
import java.util.Objects;

import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpRequest extends HttpMessage {

    private HttpMethod.Method method = null;
    private String requestTarget = null;
    private HttpVersion bestCompatableVersion = null;
    private String requestedVersion = null;

    // Headers
    private HttpHeaders headers = null;

    HttpRequest() {
    }

    public HttpMethod.Method getMethod() {
        return this.method;
    }

    void setMethod(HttpMethod.Method method) {
        this.method = method;
    }

    public String getRequestTarget() {
        return this.requestTarget;
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget.length() == 0 || requestTarget.charAt(0) != '/')
            throw new HttpParsingException("Invalid request target", HttpStatusCode.SERVER_ERROR_INTERNAL_SERVER_ERROR);
        this.requestTarget = requestTarget;
    }

    public String getRequestedVersion() {
        return this.requestedVersion;
    }

    public HttpVersion bestCompatableVersion() {
        return this.bestCompatableVersion;
    }

    void setVersion(String version) throws HttpParsingException {

        Objects.requireNonNull(version, "HTTP version cannot be null");

        if (version.isEmpty() || !version.startsWith("HTTP/")) {
            throw new HttpParsingException("Invalid HTTP version", HttpStatusCode.SERVER_ERROR_INTERNAL_SERVER_ERROR);
        }

        this.requestedVersion = version;

        System.out.println(version);

        this.bestCompatableVersion = HttpVersion.getBestCompatableVersion(version);
    }

}
