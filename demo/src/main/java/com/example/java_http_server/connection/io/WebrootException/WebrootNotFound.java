/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.java_http_server.connection.io.WebrootException;

public class WebrootNotFound extends Exception {

    public WebrootNotFound(String invalid_webroot_path) {
        super(invalid_webroot_path);
    }

}
