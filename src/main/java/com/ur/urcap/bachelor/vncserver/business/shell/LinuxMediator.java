/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class LinuxMediator
{
    private String ls = System.getProperty("line.separator");
    public void installVNC() throws UnsuccessfulCommandException
    {
        ShellCommandResponse response = doCommand("sudo dpkg -s x11vnc");
        if (response.getOutput().contains("Status: install ok installed"))
        {
            return;
        }
        doCommand("sudo apt-get -y install x11vnc");
        response = doCommand("sudo dpkg -s x11vnc");
        if (response.getOutput().contains("Status: install ok installed"))
        {
            return;
        }
        doCommand("sudo apt-get update");
        doCommand("sudo apt-get -y install x11vnc");
        if (!response.getOutput().contains("Status: install ok installed"))
        {
            throw new UnsuccessfulCommandException("x11vnc was not installed for unknown reasons");
        }

    }

    public ShellCommandResponse doCommand(String s)
    {
        String output = "";
        int exitValue = -1;
        Process p;
        try
        {
            p = Runtime.getRuntime().exec(s);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            while ((s = br.readLine()) != null)
            {
                output += s + "\n";
            }
            p.waitFor();
            exitValue = p.exitValue();
            p.destroy();
        }
        catch (Exception e)
        {
        }
        return new ShellCommandResponse(output, exitValue);
    }

    public void runScript(String script) //TODO: make it possible to run .sh scripts. Currently trouble with handling proper file pathing in linux.
    {
        System.out.println("test 3");
        try
        {
            System.out.println("test 4");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(script);
            proc.waitFor();
            StringBuffer output = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                output.append(line + "\n");
            }
            System.out.println("### " + output);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

}
