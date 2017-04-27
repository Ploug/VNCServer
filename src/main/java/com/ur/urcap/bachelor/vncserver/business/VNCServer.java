package com.ur.urcap.bachelor.vncserver.business;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.bachelor.vncserver.business.shell.LinuxMediator;
import com.ur.urcap.bachelor.vncserver.exceptions.UnsuccessfulCommandException;

/**
 *
 * @author frede
 */
public class VNCServer
{

    private LinuxMediator linMed;
    private Configuration configuration;
    private boolean active = false;
    private final String folderName = "x11vnc";

    public boolean isActive()
    {
        return active;
    }

    public VNCServer(Configuration configuration)
    {
        this.configuration = configuration;
        linMed = new LinuxMediator(folderName);
    }

    public static VNCServer createServer(DataModel model)
    {
        VNCServer server = new VNCServer(Configuration.createConfiguration(model));
        if (model.isSet("activeServer"))
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

    public void start() throws UnsuccessfulCommandException
    {
        linMed.setVNCPassword(configuration.getPassword());
        linMed.startVNC(configuration.isShared(), configuration.getPort(), configuration.isLogging(), folderName);
        active = true;

    }

    public void stop() throws UnsuccessfulCommandException
    {

        linMed.stopVNC();
        active = false;

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
        
        return linMed.getDataPath();
    }

}
