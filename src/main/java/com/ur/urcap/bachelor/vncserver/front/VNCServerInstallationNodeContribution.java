package com.ur.urcap.bachelor.vncserver.front;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.LabelComponent;
import com.ur.urcap.bachelor.vncserver.business.VNCServer.Configuration;
import com.ur.urcap.bachelor.vncserver.business.VNCServer.VNCServer;
import com.ur.urcap.bachelor.vncserver.business.shell.LinuxMediatorImpl;
import com.ur.urcap.bachelor.vncserver.interfaces.LinuxMediator;

public class VNCServerInstallationNodeContribution implements InstallationNodeContribution
{

    private static final String STARTSTOP_KEY = "startStopServer";
    private static final String SHARECONNECTION = "shareConnection";
    private static final String SSH = "SSH";
    private static final String DEFAULT_VALUE = "VNC Server foo";
    private static final String PASSWORD_FIELD = "passwordField";
    private static final String PORT_FIELD = "portText";
    private static final String LOGVNCACTIVITY = "logVNCActivity";
    
    private static final String SSH_LABEL = "SSHLabel";
    private static final String PASSWORRD_LABEL = "passwordLabel";

    private Configuration configuration;
    private DataModel model;

    private VNCServer server;
    private LinuxMediator linMed;

    public VNCServerInstallationNodeContribution(DataModel model)
    {
        this.model = model;
    }
    
    @Input(id = LOGVNCACTIVITY)
    private InputButton logVNCActivityButton;

    @Input(id = STARTSTOP_KEY)
    private InputButton startStopButton;

    @Input(id = SSH)
    private InputButton sshButton;

    @Input(id = SHARECONNECTION)
    private InputButton shareConnection;

    @Label(id = "ip")
    private LabelComponent IPAddress;

    @Label(id = PASSWORRD_LABEL)
    private LabelComponent passwordLabel;

    @Label(id = "Online")
    private LabelComponent onlineLabel;

    @Label(id = "SSHLabel")
    private LabelComponent SSHLabel;

    @Label(id = "portLabel")
    private LabelComponent portLabel;

    @Label(id = "Shared")
    private LabelComponent sharedLabel;

    @Input(id = PASSWORD_FIELD)
    private InputTextField passwordField;

    @Input(id = PORT_FIELD)
    private InputTextField portField;

    @Input(id = STARTSTOP_KEY)
    public void onStartStopClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            server.start();
            IPAddress.getText();
            IPAddress.setText(linMed.getIP());
            onlineLabel.setText("ONLINE");
            passwordLabel.setText(server.getConfig().getPassword());
            portLabel.setText(server.getConfig().getPort() + "");
            SSHLabel.setText(server.getConfig().isSSH() + "");
            sharedLabel.setText(server.getConfig().isShared() + "");
        }
    }
    
    @Input(id = LOGVNCACTIVITY)
    public void onLogVNCActivityClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            
        }
    }

//    @Input(id = STOP_KEY)
//    public void onStopClick(InputEvent event)
//    {
//        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
//        {
//            server.stop();
//            onlineLabel.setText("OFFLINE");
//        }
//    }

    @Input(id = SSH)
    public void onSSHClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            configuration.setSSH(!configuration.isSSH());

        }
    }

    @Input(id = PASSWORD_FIELD)
    public void onPasswordFieldEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            configuration.setPassword(passwordField.getText());
        }
    }

    @Input(id = PORT_FIELD)
    public void onPortFieldEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            try
            {
                configuration.setPort(Integer.parseInt(portField.getText()));

            }

            catch (NumberFormatException ex)
            {
                return;
            }

        }
    }

    @Input(id = SHARECONNECTION)
    public void onShareConnectionClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            configuration.setShared(!configuration.isShared());
        }
    }

    @Override
    public void openView()
    {

        server = new VNCServer();
        linMed = new LinuxMediatorImpl();
        configuration = new Configuration();
        startStopButton.setText("Start");
        shareConnection.setText("Share connection");
        sshButton.setText("SSH");
        passwordLabel.setText(configuration.getPassword());
        IPAddress.getText();
        IPAddress.setText(linMed.getIP());
        onlineLabel.setText("OFFLINE");
        portField.setText("5900");
        configuration.setPort(5900);
        passwordField.setText(Configuration.DEFAULT_PASSWORD);
        sharedLabel.setText(configuration.isShared() + "");
        SSHLabel.setText(configuration.isSSH() + "");
        logVNCActivityButton.setText("Log VNC Activity");
    }

    @Override
    public void closeView()
    {
    }

    @Override
    public void generateScript(ScriptWriter writer)
    {

    }

}
