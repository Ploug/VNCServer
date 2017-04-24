/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.VNCServer;


/**
 *
 * @author frede
 */
public class Configuration {
    
    public static final String DEFAULT_PASSWORD = "urvnc4321"; // Should force users to change password..
    private String password; 
    private boolean shared;
    private int port;
    private boolean logging;

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }
    private boolean SSH;
    
    public Configuration()
    {
        password = DEFAULT_PASSWORD;
        shared = false;
        SSH = false;
    }
    public Configuration(String inputPassword)
    {
        password = inputPassword;
        shared = false;
        SSH = false;
    }

    public boolean isSSH()
    {
        return SSH;
    }

    public void setSSH(boolean SSH)
    {
        this.SSH = SSH;
    }
    
    public boolean isShared()
    {
        return shared;
    }

    public void setShared(boolean shared)
    {
        this.shared = shared;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String inputPassword)
    {
        this.password = inputPassword;
    }

    public void setLogging(boolean value)
    {
        logging = value;
    }
    public boolean isLogging()
    {
        return logging;
    }

}
