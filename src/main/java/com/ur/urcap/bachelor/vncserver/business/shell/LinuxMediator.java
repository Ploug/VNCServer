/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.shell;

import com.ur.urcap.bachelor.vncserver.exceptions.UnsuccessfulCommandException;
import com.ur.urcap.bachelor.vncserver.services.ShellComService;
import com.ur.urcap.bachelor.vncserver.services.ShellCommandResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final String[] NEEDED_PROGRAMS =
    {
        "x11vnc"
    };
    private String elevated;
    private ShellComService shellCom;
    private String dataPath;

    public LinuxMediator(String dataFolder)
    {
        dataPath = fs + "var" + fs + dataFolder + fs;
        shellCom = new ShellCommunicator();
        passwdPath = dataPath + "passwd";
        ShellCommandResponse response;
        try
        {
            response = shellCom.doCommand("whoami");
            if (response.getOutput().contains("root"))
            {
                elevated = "";
            }
            else
            {
                elevated = "sudo ";
            }
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(LinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean isInstalled(String program)
    {
        try
        {
            ShellCommandResponse response = shellCom.doCommand(elevated + "dpkg -s " + program);
            return response.getOutput().contains("Status: install ok installed");
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(LinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean install(String program) throws UnsuccessfulCommandException
    {
        if (isInstalled(program))
        {
            return false;
        }
        shellCom.doCommand(elevated + "apt-get -y install " + program);
        if (isInstalled(program))
        {
            return true;
        }
        commentSources(false);
        shellCom.doCommand(elevated + "apt-get update");
        shellCom.doCommand(elevated + "apt-get -y install " + program);
        commentSources(true);
        if (!isInstalled(program))
        {
            throw new UnsuccessfulCommandException(program + " was not installed for unknown reasons");
        }
        return true;

    }

    public void setVNCPassword(String password) throws UnsuccessfulCommandException
    {

        File f = new File(passwdPath);
        shellCom.doCommand(elevated + "mkdir -p " + passwdPath.replace(fs + "passwd", ""));
        shellCom.doCommand(elevated + "touch " + passwdPath);
        ShellCommandResponse response = shellCom.doCommand(elevated + "x11vnc --storepasswd " + password + " " + passwdPath);

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

        shellCom.doCommand("x11vnc -R stop");
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

            shellCom.doCommand(elevated + "touch" + f.getAbsolutePath());

            String logPath = f.getAbsolutePath();
            command += " -o " + logPath;
        }
        System.out.println("COMMAND: " + command);
        shellCom.doCommand(command);
    }

    public void stopVNC() throws UnsuccessfulCommandException
    {
        ShellCommandResponse response = shellCom.doCommand("x11vnc -R stop");
        System.out.println("output: " + response.getOutput());
        System.out.println("exit value: " + response.getExitValue());
        if (response.getExitValue() != 0)
        {
            throw new UnsuccessfulCommandException("Something went wrong when stoppping the server");
        }
    }

    public boolean installMissingPrograms() throws UnsuccessfulCommandException
    {
        boolean installedAnything = false;
        for (String neededProgram : NEEDED_PROGRAMS)
        {
            installedAnything = install(neededProgram) ? true : installedAnything;
        }
        return installedAnything;
    }

    public boolean neededProgramsInstalled()
    {
        for (String neededProgram : NEEDED_PROGRAMS)
        {
            if (!isInstalled(neededProgram))
            {
                return false;
            }
        }
        return true;
    }

    public String getIP()
    {
        ShellCommandResponse response;
        try
        {
            response = shellCom.doCommand("ip addr show");
            String s = response.getOutput();
            String firstEth = s.substring(s.indexOf("eth"));
            int startIndex = firstEth.indexOf("inet");
            return firstEth.substring(startIndex + 5, firstEth.indexOf("/", startIndex));
        }
        catch (UnsuccessfulCommandException ex)
        {
            Logger.getLogger(LinuxMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public String getDataPath()
    {
        return dataPath;
    }

}
