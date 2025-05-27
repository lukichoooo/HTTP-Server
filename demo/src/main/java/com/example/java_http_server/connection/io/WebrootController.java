package com.example.java_http_server.connection.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

import com.example.java_http_server.connection.io.WebrootException.FileReadingException;
import com.example.java_http_server.connection.io.WebrootException.WebrootNotFound;

public class WebrootController {

    private File webroot;

    public WebrootController(String webrootPath) throws WebrootNotFound {
        this.webroot = new File(webrootPath);
        if (!webroot.exists()) {
            throw new WebrootNotFound("Invalid webroot path");
        }
    }

    private boolean pathEndsWithSlash(String relativePath) {
        return relativePath.endsWith("/");
    }

    private boolean relativePathExists(String relativePath) {
        return new File(webroot, relativePath).exists();
    }

    // used to populate header CONTENT-TYPE (default header for missing headers)
    public String getMimeType(String relativePath) throws FileNotFoundException {
        if (pathEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }
        if (!relativePathExists(relativePath)) {
            throw new FileNotFoundException("File not found: " + relativePath);
        }

        File file = new File(webroot, relativePath);

        String mimetype = URLConnection.getFileNameMap().getContentTypeFor(file.getName());

        return (mimetype != null ? mimetype : "application/octet-stream");
    }

    public byte[] readFile(String relativePath) throws FileNotFoundException, FileReadingException {
        if (pathEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }
        if (!relativePathExists(relativePath)) {
            throw new FileNotFoundException("File not found: " + relativePath);
        }

        File file = new File(webroot, relativePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        try {
            fileInputStream.read(fileBytes);
        } catch (IOException ex) {
            throw new FileReadingException("Couldnt read file: " + relativePath);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                throw new FileReadingException("Couldnt read file: " + relativePath);
            }
        }
        return fileBytes;
    }
}
