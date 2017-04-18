/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.impl;

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

    public void test()
    {
        System.out.println("test 1");
        String fs = System.getProperty("file.separator");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("/com/ur/urcap/bachelor/vncserver/scripts/Test.sh").getFile());
        System.out.println(" absolute path:" + file.getAbsolutePath());
        runScript(file.getPath().replace("/", fs));
        runScript("com/ur/urcap/bachelor/vncserver/scripts/Test.sh".replace("/", fs));
        runScript("/com/ur/urcap/bachelor/vncserver/scripts/Test.sh".replace("/", fs));
        runScript(classLoader.getResource("/com/ur/urcap/bachelor/vncserver/scripts/Test.sh").toString().replace("/", fs));
        System.out.println("test 2");
    }

    public void runScript(String script)
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

    public void doCommand(String s)
    {
        Process p;
        try
        {
            p = Runtime.getRuntime().exec(s);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
            {
                System.out.println("line: " + s);
            }
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        }
        catch (Exception e)
        {
        }
    }
}
