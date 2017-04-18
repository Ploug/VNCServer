/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.shell;

/**
 *
 * @author frede
 */
public class ShellCommandResponse
{
    private String output;
    private int exitValue;
    
    public ShellCommandResponse(String output, int exitValue)
    {
        this.output = output;
        this.exitValue = exitValue;
    }
    public String getOutput()
    {
        return output;
    }

    public int getExitValue()
    {
        return exitValue;
    }

    
}
