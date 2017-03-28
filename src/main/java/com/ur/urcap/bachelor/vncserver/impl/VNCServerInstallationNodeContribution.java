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
    private static final String DEFAULT_VALUE = "VNC Server foo";

    private DataModel model;

    public VNCServerInstallationNodeContribution(DataModel model)
    {
        this.model = model;
    }
   

    @Input(id = START_KEY)
    private InputButton startButton;
    
    @Input(id = STOP_KEY)
    private InputButton stopButton;

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
    }

    @Override
    public void closeView()
    {
    }


    @Override
    public void generateScript(ScriptWriter writer)
    {
       
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
