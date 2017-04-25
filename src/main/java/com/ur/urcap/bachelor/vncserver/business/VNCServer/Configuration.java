/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.business.VNCServer;

import com.ur.urcap.api.domain.data.DataModel;

/**
 *
 * @author frede
 */
public class Configuration
{

    public static final String DEFAULT_PASSWORD = "urvnc4321"; // Should force users to change password..

    private String password;
    private boolean SSH;
    private boolean shared;
    private int port;
    private boolean logging;

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public Configuration()
    {
        password = DEFAULT_PASSWORD;
        shared = false;
        SSH = false;
    }

    public Configuration(String inputPassword)
    {
        password = inputPassword;
        shared = false;
        SSH = false;
    }

    public static Configuration createConfiguration(DataModel model)
    {
        if (model.isSet("config"))
        {
            Configuration retval = new Configuration();
            String[] persisConfig = model.get("config", new String[0]);
            if (persisConfig.length != 5)
            {
                return new Configuration();
            }

            retval.setSSH(persisConfig[0].equals("true"));
            retval.setShared(persisConfig[1].equals("true"));
            retval.setLogging(persisConfig[2].equals("true"));
            retval.setPassword(persisConfig[3]);
            retval.setPort(Integer.parseInt(persisConfig[4]));
            return retval;
        }
        else
        {
            return new Configuration();
        }
    }

    public void persist(DataModel model)
    {
        String[] persisConfig = new String[5];
        persisConfig[0] = isSSH() + "";
        persisConfig[1] = isShared() + "";
        persisConfig[2] = isLogging() + "";
        persisConfig[3] = getPassword();
        persisConfig[4] = getPort() + "";
        model.set("config", persisConfig);

    }

    public boolean isSSH()
    {
        return SSH;
    }

    public void setSSH(boolean SSH)
    {
        this.SSH = SSH;
    }

    public boolean isShared()
    {
        return shared;
    }

    public void setShared(boolean shared)
    {
        this.shared = shared;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String inputPassword)
    {
        this.password = inputPassword;
    }

    public void setLogging(boolean value)
    {
        logging = value;
    }

    public boolean isLogging()
    {
        return logging;
    }

}
