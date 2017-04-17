package com.ur.urcap.bachelor.vncserver.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;

public class VNCServerInstallationNodeContribution implements InstallationNodeContribution
{

    private static final String START_KEY = "startserver";
    private static final String STOP_KEY = "stopserver";
    private static final String CHANGE_PASSWORD = "changepassword";
    private static final String SHARECONNECTION = "shareConnection";
    private static final String PORT = "port";
    private static final String SSH = "SSH";
    private static final String DEFAULT_VALUE = "VNC Server foo";

    private DataModel model;

    public VNCServerInstallationNodeContribution(DataModel model)
    {
        this.model = model;
    }
   

    @Input(id = START_KEY)
    private InputButton startButton;
    
    @Input(id = SSH)
    private InputButton sshButton;
    
    @Input(id = STOP_KEY)
    private InputButton stopButton;
    
    @Input(id = CHANGE_PASSWORD)
    private InputButton changePassword;
    
    @Input(id = SHARECONNECTION)
    private InputButton shareConnection;
    
    @Input(id = PORT)
    private InputButton port;

    @Input(id = START_KEY)
    public void onStartClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            System.out.println("Hello start :)");
        }
    }
     @Input(id = STOP_KEY)
    public void onStopClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            System.out.println("Hello stop:)");
        }
    }

    @Override
    public void openView()
    {
        startButton.setText("Start");
        stopButton.setText("Stop");
        changePassword.setText("Change Password");
        shareConnection.setText("Share connection");
        sshButton.setText("SSH");
        port.setText("Choose port");
    }

    @Override
    public void closeView()
    {
    }


    @Override
    public void generateScript(ScriptWriter writer)
    {
     writer.appendRaw("function toggle_visibility(id) {\n" +
"                var container = document.getElementById('changePass');\n" +
"\n" +
"                if (container.style.display == 'block')\n" +
"                    container.style.display = 'none';\n" +
"                else\n" +
"                    container.style.display = 'block';\n" +
"            }");
    }

   
    
     public String stopserver()
    {
        return model.get(START_KEY, DEFAULT_VALUE);
    }

    private void stopserver(String message)
    {
        model.set(START_KEY, message);
    }
    public String startserver()
    {
        return model.get(START_KEY, DEFAULT_VALUE);
    }

    private void startserver(String message)
    {
        model.set(START_KEY, message);
    }

}
