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
public class VNCServer {
    
    private LinuxMediator linMed;
    public static final String DEFAULT_PASSWORD = "urvnc4321"; // Should force users to change password..
    public VNCServer()
    {
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
    
    public void start()
    {
        try
        {
            linMed.startVNC(false, 5900, false, true);
        }
        catch (UnsuccessfulCommandException ex)
        {
            System.out.println("Something went wrong in starting");
        }
    }
    
    public void stop()
    {
         linMed.doCommand("");
    }
}
