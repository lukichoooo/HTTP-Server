package com.example.java_http_server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.java_http_server.connection.exceptions.ListenerSocketException;

public class SocketListenerThread extends Thread {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SocketListenerThread.class);

    private ServerSocket serverSocket;
    private int port;
    private String webroot;

    public SocketListenerThread(int port, String webroot) {
        this.port = port;
        this.webroot = webroot;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                LOGGER.info(
                        getName() + " accepted connection from " + socket.getInetAddress() + ":" + port);

                SocketThread socketThread = new SocketThread(socket, webroot);
                socketThread.start();
            }
        } catch (IOException e) {
            throw new ListenerSocketException("SocketListenerThread failed", e);
        } finally {
            try {
                serverSocket.close();
                LOGGER.info("SocketListenerThread closed");
            } catch (IOException e) {
                throw new ListenerSocketException("SocketListenerThread unable to close", e);
            }
        }
    }
}
