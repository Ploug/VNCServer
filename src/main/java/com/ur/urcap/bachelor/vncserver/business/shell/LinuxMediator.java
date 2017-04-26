/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.shell;

import com.ur.urcap.api.ui.component.LabelComponent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class LinuxMediator
{

    //TODO check if running as root already.
    private String ls = System.getProperty("line.separator");
    private String fs = System.getProperty("file.separator");
    private String passwdPath;
    private String dataPath;
    private String elevated;

    public LinuxMediator(String dataPath)
    {
        this.dataPath = dataPath;
        passwdPath = dataPath + "passwd";
        ShellCommandResponse response = doCommand("whoami");

        if (response.getOutput().contains("root"))
        {
            elevated = "";
        }
        else
        {
            elevated = "sudo ";
        }
    }

    private void commentSources(boolean value)
    {
        if (value)
        {
            findReplaceInFile("/etc/apt/sources.list", "deb ", "#deb ");
            findReplaceInFile("/etc/apt/sources.list", "deb-", "#deb-");
        }
        else
        {
            findReplaceInFile("/etc/apt/sources.list", "#deb", "deb");
        }
    }

    private void findReplaceInFile(String path, String find, String replace)
    {

        try
        {
            File f1 = new File(path);
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            String line;
            List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null)
            {
                line = line.replaceAll(find, replace);
                lines.add(line + ls);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
            {
                out.write(s);
            }
            out.flush();
            out.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(LinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(LinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean VNCInstalled()
    {
        ShellCommandResponse response = doCommand(elevated + "dpkg -s x11vnc");
        return response.getOutput().contains("Status: install ok installed");
    }

    public void installVNC() throws UnsuccessfulCommandException
    {
        if (VNCInstalled())
        {
            return;
        }
        doCommand(elevated + "apt-get -y install x11vnc");
        if (VNCInstalled())
        {
            return;
        }
        commentSources(false);
        doCommand(elevated + "apt-get update");
        doCommand(elevated + "apt-get -y install x11vnc");
        commentSources(true);
        if (!VNCInstalled())
        {
            throw new UnsuccessfulCommandException("x11vnc was not installed for unknown reasons");
        }

    }

    public void setVNCPassword(String password) throws UnsuccessfulCommandException
    {

        File f = new File(passwdPath);
        doCommand(elevated + "mkdir -p " + passwdPath.replace(fs + "passwd", ""));
        doCommand(elevated + "touch " + passwdPath);
        ShellCommandResponse response = doCommand(elevated + "x11vnc --storepasswd " + password + " " + passwdPath);

        if (!response.getOutput().contains("stored passwd in file"))
        {
            throw new UnsuccessfulCommandException("Password was not saved properly");
        }
    }

    public void startVNC(boolean shared, int port, boolean log, String dataPath) throws UnsuccessfulCommandException
    {
        /*
         -forever: keeps server running after you log out
         - nodpms: prevents power management saving and keeps display alive
         - noxdamage: prevents framebuffer issues and lets x11vnc run with screen tearing issues.
         - rfbport: 5900 is the default port for vnc
         - display:0 chooses which display to show.
         - bg : runs process in background
         - o: location to log the shit.
         - rfbauth: location password is stored.

         */

        doCommand("x11vnc -R stop");
        String command = elevated + "" + fs + "usr" + fs + "bin" + fs + "x11vnc -forever -nodpms -noxdamage -bg -rfbauth " + passwdPath;
        command += shared ? " -shared" : "";
        command += " -rfbport " + port;
        if (log)
        {
            File f;
            int i = 0;
            do
            {
                f = new File(dataPath + "logFile_" + i + ".log");
                i++;
            }
            while (f.exists());

            doCommand(elevated + "touch" + f.getAbsolutePath());

            String logPath = f.getAbsolutePath();
            command += " -o " + logPath;
        }
        System.out.println("COMMAND: " + command);
        doCommand(command);
    }

    public void stopVNC() throws UnsuccessfulCommandException
    {
        ShellCommandResponse response = doCommand("x11vnc -R stop");
        System.out.println("output: " + response.getOutput());
        System.out.println("exit value: " + response.getExitValue());
        if (response.getExitValue() != 0)
        {
            throw new UnsuccessfulCommandException("Something went wrong when stoppping the server");
        }
    }

    /*
    public ShellCommandResponse doCommands(String[] s)
    {
        String output = "";
        int exitValue = -1;
        Process p;
        try
        {
            p = Runtime.getRuntime().exec(s[0]);
            OutputStream opst = p.getOutputStream();
            PrintWriter pw = new PrintWriter(opst);
            for (int i = 1; i < s.length; i++)
            {
                pw.write(s[i] + "\n");
                System.out.println(s[i] + "\n");
            }
            System.out.println("Hello :)");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            System.out.println("Helldso :)" + p.getInputStream().available());
            String line = "";
            while ((line = br.readLine()) != null)
            {
                output += line + "\n";
            }

            p.waitFor();
            exitValue = p.exitValue();
            p.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ShellCommandResponse(output, exitValue);
    }
     */
    private ShellCommandResponse doCommand(String s)
    {
        String output = "";
        int exitValue = -1;
        Process p;
        try
        {
            System.out.println("\ncommand:" + s + "\n");
            p = Runtime.getRuntime().exec(s);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            BufferedReader ir = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));
            while ((s = ir.readLine()) != null)
            {
                output += s + "\n";
            }
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
            e.printStackTrace();
        }
        return new ShellCommandResponse(output, exitValue);
    }

    public void runScript(String script) //TODO: make it possible to run .sh scripts. Currently trouble with handling proper file pathing in linux.
    {
        try
        {
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

    public String getIP()
    {
        ShellCommandResponse response = doCommand("ip addr show");
        String s = response.getOutput();
        String firstEth = s.substring(s.indexOf("eth"));
        int startIndex = firstEth.indexOf("inet");
        return firstEth.substring(startIndex + 5, firstEth.indexOf("/", startIndex));
    }

}
