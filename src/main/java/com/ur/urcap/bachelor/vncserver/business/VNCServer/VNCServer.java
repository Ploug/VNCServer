/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.VNCServer;

import com.ur.urcap.bachelor.vncserver.business.shell.LinuxMediatorImpl;
import com.ur.urcap.bachelor.vncserver.business.shell.UnsuccessfulCommandException;
import com.ur.urcap.bachelor.vncserver.interfaces.LinuxMediator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class VNCServer
{

    private LinuxMediator linMed;
    private Configuration configuration;
    private boolean active = false;

    public VNCServer()
    {
        this.configuration = new Configuration();

        linMed = new LinuxMediatorImpl();
        try
        {
            //Create default VNC server

            linMed.installVNC();

            linMed.setVNCPassword(this.configuration.getPassword());
        }
        catch (UnsuccessfulCommandException ex)
        {
            System.out.println("Something went wrong in setup.");
        }
    }

    public boolean isActive()
    {
        return active;
    }

    public VNCServer(Configuration configuration)
    {
        this.configuration = configuration;
        linMed = new LinuxMediatorImpl();
        try
        {
            //Create default VNC server

            linMed.installVNC();

            linMed.setVNCPassword(this.configuration.getPassword());
        }
        catch (UnsuccessfulCommandException ex)
        {
            System.out.println("Something went wrong in setup.");
        }
    }

    public void start()
    {
        try
        {
            linMed.startVNC(configuration.isShared(), configuration.getPort(), configuration.isSSH(), configuration.isLogging());
            active = true;
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
            active = false;
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(VNCServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setConfig(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public Configuration getConfig()
    {
        return configuration;
    }

}
