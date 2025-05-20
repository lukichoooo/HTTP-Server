
package com.example.java_http_server.connection.exceptions;

import java.io.IOException;

public class ListenerSocketException extends RuntimeException {

    public ListenerSocketException(String message, IOException e) {
        super(message, e);
    }

    public ListenerSocketException(String message) {
        super(message);
    }

}
