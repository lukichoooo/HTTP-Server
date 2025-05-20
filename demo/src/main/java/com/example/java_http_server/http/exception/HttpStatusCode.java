package com.example.java_http_server.http.exception;

public enum HttpStatusCode {

    // CLIENT ERRORS
    CLIENT_ERROR_OK(200, "OK"),
    CLIENT_ERROR_CREATED(201, "Created"),
    CLIENT_ERROR_NO_CONTENT(204, "No Content"),
    CLIENT_ERROR_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_NOT_FOUND(404, "Not Found"),
    // SERVER ERRORS
    SERVER_ERROR_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_METHOD_NOT_ALLOWED(501, "Method Not Allowed"),
    CLIENT_ERROR_HTTP_VERSION_NOT_SUPPORTED(505, "Unsupported HTTP Version");

    private final int code;
    private final String message;

    HttpStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
}
