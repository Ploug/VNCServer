package com.ur.urcap.bachelor.vncserver.front;

import com.ur.urcap.api.contribution.InstallationNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{

    public void start(BundleContext context) throws Exception
    {
        VNCServerInstallationNodeService netSecInstall = new VNCServerInstallationNodeService();

        context.registerService(InstallationNodeService.class, netSecInstall, null);
    }

    public void stop(BundleContext context) throws Exception
    {
        // TODO add deactivation code here
    }

}
