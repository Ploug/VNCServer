/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class NewClass
{
    public static  void main (String[] ags)
    {
       
        NewClass test = new NewClass();
        test.call();
        test.runScript("/com/ur/urcap/bachelor/vncserver/scripts/Test.sh");
    }
    public void runScript(String script)
    {
         try
        {
// String target = new String("mkdir stackOver");
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
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void call()
    {
        System.out.println("sup");
        File f = new File("/com/ur/urcap/bachelor/vncserver/scripts/Test.sh");
        File f1 = new File("/com/ur/urcap/bachelor/vncserver/impl/installation.html");
        
        InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/bachelor/vncserver/scripts/Test.sh");
        try
        {
            System.out.println(is.available());
        }
        catch (IOException ex)
        {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(f.exists());
        System.out.println(f1.exists());
    }
}
