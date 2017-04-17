/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.impl;

/**
 *
 * @author frede
 */
public class VNCServer {
    
    private LinuxMediator linMed;
    public VNCServer()
    {
        linMed = new LinuxMediator();
        //Create default VNC server
         linMed.doCommand("s");
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
