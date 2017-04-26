/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.VNCServer;

import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.ui.component.LabelComponent;
import com.ur.urcap.bachelor.vncserver.business.shell.LinuxMediator;
import com.ur.urcap.bachelor.vncserver.business.shell.UnsuccessfulCommandException;
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
    private String fs = System.getProperty("file.separator");
    private String dataPath = fs + "var" + fs + "x11vnc" + fs;

    public boolean isActive()
    {
        return active;
    }

    public VNCServer(Configuration configuration)
    {
        this.configuration = configuration;
        linMed = new LinuxMediator(dataPath);
    }

    public static VNCServer createServer(DataModel model)
    {
        VNCServer server = new VNCServer(Configuration.createConfiguration(model));
        if(model.isSet("activeServer"))
        {
            server.active = model.get("activeServer", false);
        }
        return server;
    }
    public void persist(DataModel model)
    {
        model.set("activeServer", active);
        configuration.persist(model);
    }
    public void start()
    {
        try
        {
            linMed.setVNCPassword(configuration.getPassword());
            linMed.startVNC(configuration.isShared(), configuration.getPort(), configuration.isLogging(), dataPath);
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

    public String getDataPath()
    {
        return dataPath;
    }

}
