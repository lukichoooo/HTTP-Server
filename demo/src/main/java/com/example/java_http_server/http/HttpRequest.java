package com.example.java_http_server.http;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpRequest extends HttpMessage {

    private Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);

    private HttpMethod.Method method = null;
    private String requestTarget = null;

    private HttpVersion bestCompatableVersion = null;
    private String requestedVersion = null;

    private HttpHeaders headers = new HttpHeaders();

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

    public void setHttpHeader(String header) throws HttpParsingException {
        if (header == null || header.isEmpty())
            return;
        this.headers.addHeader(header);
    }

    public String getHttpHeaderValue(String name) throws HttpParsingException {
        return this.headers.getHeaderValue(name);
    }

}
