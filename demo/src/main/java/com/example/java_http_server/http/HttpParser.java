package com.example.java_http_server.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.java_http_server.http.exception.HttpParsingException;
import com.example.java_http_server.http.exception.HttpStatusCode;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20;
    private static final int CR = 0x0d;
    private static final int LF = 0x0a;

    public HttpParser() {
        // TODO Auto-generated constructor stub
    }

    public HttpRequest parseRequest(InputStream inStream) throws IOException, HttpParsingException {
        try {
            InputStreamReader reader = new InputStreamReader(inStream);

            HttpRequest request = new HttpRequest();

            parseRequestLine(reader, request);
            parseHeaders(reader, request);
            parseBody(reader, request);

            return request;
        } catch (HttpParsingException ex) {
            throw new HttpParsingException(ex.getMessage(), ex.getErrorCode());
        }
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request)
            throws IOException, HttpParsingException {
        boolean methodParsed = false;
        boolean versionParsed = false;
        boolean requestTargetParsed = false;

        StringBuilder requestLine = new StringBuilder();
        int _byte = -1;
        while ((_byte = reader.read()) != -1) { // read next byte input

            if (_byte == CR) {
                if (!methodParsed || !requestTargetParsed) {
                    throw new HttpParsingException("HTTP RequestLine too few tokens: " + requestLine.toString(),
                            HttpStatusCode.CLIENT_ERROR_NO_CONTENT);
                }
                if (!versionParsed) {
                    request.setVersion(requestLine.toString());
                    requestLine.delete(0, requestLine.length());
                    versionParsed = true;
                }
                if ((_byte = reader.read()) == LF) {
                    if (!methodParsed || !requestTargetParsed) {
                        throw new HttpParsingException(
                                "HTTP RequestLine too few tokens: " + requestLine.toString(),
                                HttpStatusCode.CLIENT_ERROR_NO_CONTENT);
                    }
                    return; // we recived CRLF
                } else
                    throw new HttpParsingException(
                            "HTTP Expected LF: " + requestLine.toString(),
                            HttpStatusCode.CLIENT_ERROR_BAD_REQUEST);
            }

            if (_byte == SP) { // before "SP" is method
                if (!methodParsed) {
                    request.setMethod(HttpMethod.valueOfMethod(requestLine.toString())); // set method type
                    methodParsed = true;
                    requestLine.delete(0, requestLine.length()); // clear requestLine
                } else if (!requestTargetParsed) {
                    request.setRequestTarget(requestLine.toString());
                    requestLine.delete(0, requestLine.length());
                    requestTargetParsed = true;
                } else if (versionParsed) { // everything is parsed But we encountered "SP"
                    throw new HttpParsingException(
                            "HTTP RequestLine too many tokens: " + requestLine.toString(),
                            HttpStatusCode.SERVER_ERROR_METHOD_NOT_ALLOWED);
                }
            } else {
                if (!methodParsed && requestLine.length() > HttpMethod.MAX_METHOD_LENGTH) {
                    throw new HttpParsingException(
                            "HTTP Method too long: " + requestLine.toString(),
                            HttpStatusCode.SERVER_ERROR_METHOD_NOT_ALLOWED);
                }
                requestLine.append((char) _byte); // append the current byte to StringBuilder
            }
        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) throws HttpParsingException { // TODO fix this shi

        StringBuilder headerLine = new StringBuilder();
        int _byte = -1;
        try {
            while ((_byte = reader.read()) != -1) {
                if (_byte == CR) {
                    if ((_byte = reader.read()) == LF) {
                        if (headerLine.length() == 0) {
                            return;
                        }
                        request.setHttpHeader(headerLine.toString());
                        headerLine.delete(0, headerLine.length());
                        continue; //  so we dont append LF to headerLine
                    }
                }
                headerLine.append((char) _byte);
            }
        } catch (IOException e) {
            throw new HttpParsingException("Failed to read headers", HttpStatusCode.SERVER_ERROR_INTERNAL_SERVER_ERROR);
        }
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {
        // TODO Auto-generated method stub
    }

}
