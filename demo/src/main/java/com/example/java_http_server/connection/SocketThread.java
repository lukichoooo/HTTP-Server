package com.example.java_http_server.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.example.java_http_server.connection.exceptions.ConnectedSocketException;
import com.example.java_http_server.http.HttpParser;
import com.example.java_http_server.http.exception.HttpParsingException;

public class SocketThread extends Thread {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SocketThread.class);

    private Socket socket = null;
    private String webroot = null;
    private HttpParser httpParser = new HttpParser();

    private InputStream inStream;
    private OutputStream outStream;

    public SocketThread(Socket socket, String webroot) {
        this.socket = socket;
        this.webroot = webroot;
    }

    @Override
    public void run() {
        try {
            inStream = socket.getInputStream();
            outStream = socket.getOutputStream();

            String html = "<!DOCTYPE html><html><head><title>america Ya:3</title></head><body><h1>Hello!</h1></body></html>";
            String CRLF = "\r\n";
            String HTTP_response = "HTTP/1.1 200 OK" + CRLF +
                    "Content-Type: text/html; charset=UTF-8" + CRLF +
                    "Content-Length: " + html.getBytes().length + CRLF +
                    CRLF +
                    html;

            try {
                httpParser.parseRequest(inStream);
            } catch (HttpParsingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            outStream.write(HTTP_response.getBytes());

        } catch (IOException e) {
            throw new ConnectedSocketException("SocketThread connection failed", e);
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
                if (inStream != null)
                    inStream.close();
                if (socket != null)
                    socket.close();
                LOGGER.info("SocketThread closed");
            } catch (IOException e) {
                throw new ConnectedSocketException("SocketThread unable to close", e);
            }
        }
    }
}
