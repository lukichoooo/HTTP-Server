package com.example.java_http_server.connection.exceptions;

public class ConnectedSocketException extends RuntimeException {

    public ConnectedSocketException(String message) {
        super(message);
    }

    public ConnectedSocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
