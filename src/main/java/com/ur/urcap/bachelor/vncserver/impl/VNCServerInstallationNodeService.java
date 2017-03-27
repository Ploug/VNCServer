package com.ur.urcap.bachelor.vncserver.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import java.io.InputStream;

public class VNCServerInstallationNodeService implements InstallationNodeService
{

    public VNCServerInstallationNodeService()
    {
      
    }

    @Override
    public InstallationNodeContribution createInstallationNode(URCapAPI api, DataModel model)
    {
        return new VNCServerInstallationNodeContribution(model);
    }

    @Override
    public String getTitle()
    {
        return "VNC Server";
    }

    @Override
    public InputStream getHTML()
    {
        InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/bachelor/vncserver/impl/installation.html");
        return is;
    }
}
