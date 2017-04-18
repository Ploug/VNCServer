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
    public VNCServer()
    {
        linMed = new LinuxMediator();
        try
        {
            //Create default VNC server
            linMed.installVNC();
        }
        catch (UnsuccessfulCommandException ex)
        {
            // Tell user the server could not be created
        }
    }
    
    public void start()
    {
        linMed.doCommand("");
    }
    
    public void stop()
    {
         linMed.doCommand("");
    }
}
