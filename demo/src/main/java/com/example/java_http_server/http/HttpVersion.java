package com.example.java_http_server.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static Pattern pattern = Pattern.compile("HTTP/([0-9]+)\\.([0-9]+)");
    private static Matcher matcher = pattern.matcher("");

    public static HttpVersion getBestCompatableVersion(String literalVersion) throws HttpParsingException {

        if (literalVersion == null || literalVersion.isEmpty()) {
            throw new HttpParsingException("HTTP version is empty or null",
                    HttpStatusCode.CLIENT_ERROR_HTTP_VERSION_NOT_SUPPORTED);
        }
        for (HttpVersion version : HttpVersion.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                return version;
            }
            matcher.reset(literalVersion);
            if (matcher.matches()) {
                int major = Integer.parseInt(matcher.group(1));
                int minor = Integer.parseInt(matcher.group(2));
                if (major == version.MAJOR && minor <= version.MINOR) {
                    return version;
                }
            }
        }
        throw new HttpParsingException("Invalid HTTP version", HttpStatusCode.CLIENT_ERROR_HTTP_VERSION_NOT_SUPPORTED);
    }
}
