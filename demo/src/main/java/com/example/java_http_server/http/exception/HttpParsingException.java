package com.example.java_http_server.http.exception;

public class HttpParsingException extends Exception {

    // returns HttpStatusCode
    private HttpStatusCode errorCode;

    public HttpParsingException(String message, HttpStatusCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }

}
