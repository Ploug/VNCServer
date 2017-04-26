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
    private boolean shared;
    private int port;
    private boolean logging;

   

    public Configuration()
    {
        password = DEFAULT_PASSWORD;
        port = 5900;
        shared = false;
        logging = false;
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

            retval.setShared(persisConfig[0].equals("true"));
            retval.setLogging(persisConfig[1].equals("true"));
            retval.setPassword(persisConfig[2]);
            try
            {
                retval.setPort(Integer.parseInt(persisConfig[3]));
            }
            catch(NumberFormatException ex)
            {
                retval.setPort(5900);
            }
            
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
        persisConfig[0] = isShared() + "";
        persisConfig[1] = isLogging() + "";
        persisConfig[2] = getPassword();
        persisConfig[3] = getPort() + "";
        model.set("config", persisConfig);

    }

    
     public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
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
