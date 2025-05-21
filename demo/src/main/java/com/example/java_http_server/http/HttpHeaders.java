package com.example.java_http_server.http;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpHeaders {

    private HashMap<String, String> headersMap = new HashMap<>();

    private Pattern pattern = Pattern.compile("(?m)^([\\w-]+):\\s*(.+)$");

    public void addHeader(String header) throws HttpParsingException {

        Matcher matcher = pattern.matcher(header);
        if (matcher.find()) {
            String name = matcher.group(1);
            String value = matcher.group(2);
            headersMap.put(name, value);
        } else
            throw new HttpParsingException("Invalid header", HttpStatusCode.CLIENT_ERROR_BAD_REQUEST);
    }

    public String getHeaderValue(String name) {
        return this.headersMap.get(name);
    }
}
