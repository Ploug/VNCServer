/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business;

import com.ur.urcap.bachelor.vncserver.business.shell.LinuxMediator;
import com.ur.urcap.bachelor.vncserver.business.shell.UnsuccessfulCommandException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class VNCServer
{

    private LinuxMediator linMed;
    public static final String DEFAULT_PASSWORD = "urvnc4321"; // Should force users to change password..
    private String password; 
    private boolean shared;
    private boolean SSH;

   
    private int port;

    public VNCServer()
    {
        shared = false;
        SSH = false;
        linMed = new LinuxMediator();
        try
        {
            //Create default VNC server

            linMed.installVNC();

            linMed.setVNCPassword(DEFAULT_PASSWORD);
        }
        catch (UnsuccessfulCommandException ex)
        {
            System.out.println("Something went wrong in setup.");
        }
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
        if(password == null)
        {
            return DEFAULT_PASSWORD;
        }
        return password;
    }
    
    public String getIP()
    {
        return linMed.getIP();
    }
    public void setPassword(String inputPassword)
    {
        try
        {
            linMed.setVNCPassword(inputPassword);
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(VNCServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.password = inputPassword;
    }

    public void start()
    {
        try
        {
            linMed.startVNC(shared, port, false, true);
        }
        catch (UnsuccessfulCommandException ex)
        {
            System.out.println("Something went wrong in starting");
        }
    }

    public void stop()
    {
        try
        {
            linMed.stopVNC();
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(VNCServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPort(int inPort)
    {
        port = inPort; 
    }
    
    public int getPort()
    {
        return port;
    }

    public void setSSH(boolean value)
    {
        SSH = value;
    }
    public boolean isSSH()
    {
       return SSH;
    }
}
