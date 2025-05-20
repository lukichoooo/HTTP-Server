package com.example.java_http_server.http;

import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpMethod extends HttpMessage {

    public enum Method {
        GET, POST, DELETE, PUT, HEAD
    }

    static final int MAX_METHOD_LENGTH;

    static { // Static Block, to initialize static variables when the class is loaded in
             // memory
        int max = 0;
        for (Method method : Method.values()) {
            max = Math.max(max, method.name().length());
        }
        MAX_METHOD_LENGTH = max;
    }

    public static Method valueOfMethod(String strMethod) throws HttpParsingException {
        try {
            return Method.valueOf(strMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HttpParsingException("Unknown HTTP method: " + strMethod,
                    HttpStatusCode.SERVER_ERROR_METHOD_NOT_ALLOWED);
        }
    }

}