package com.ur.urcap.bachelor.vncserver.front;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;
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
    private InputButton shareConnectionButton;

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

    @Label(id = "logLabel")
    private LabelComponent logLabel;

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
            if (!server.isActive())
            {
                server.start();
                IPAddress.getText();
                IPAddress.setText(linMed.getIP());
                onlineLabel.setText("ONLINE");
                passwordLabel.setText(server.getConfig().getPassword());
                portLabel.setText(server.getConfig().getPort() + "");
                if (configuration.isSSH())
                {
                    SSHLabel.setText("SSH");
                }
                else
                {
                    SSHLabel.setText("Telnet");
                }
                sharedLabel.setText(server.getConfig().isShared() + "");
                logLabel.setText(server.getConfig().isLogging() + "");
                startStopButton.setText("Stop");
            }
            else
            {
                server.stop();
                IPAddress.setText("");
                onlineLabel.setText("OFFLINE");
                passwordLabel.setText("");
                portLabel.setText("");
                SSHLabel.setText("");
                sharedLabel.setText("");
                logLabel.setText("");
                startStopButton.setText("Start");
            }
            configuration.persist(model);
        }
    }

    @Input(id = LOGVNCACTIVITY)
    public void onLogVNCActivityClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            configuration.setLogging(!configuration.isLogging());
            logVNCActivityButton.setText("Log VNC Activity:" + configuration.isLogging());
        }
    }

    @Input(id = SSH)
    public void onSSHClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            configuration.setSSH(!configuration.isSSH());
            if (configuration.isSSH())
            {
                sshButton.setText("SSH");
            }
            else
            {
                sshButton.setText("Telnet");
            }

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
            shareConnectionButton.setText("Shared: " + configuration.isShared());
        }
    }

    @Override
    public void openView()
    {

        server = new VNCServer();
        linMed = new LinuxMediatorImpl();
        startStopButton.setText("Start");
        shareConnectionButton.setText("Share connection");
        sshButton.setText("SSH");

        configuration = Configuration.createConfiguration(model);

        portLabel.setText("");
        sharedLabel.setText("");
        SSHLabel.setText("");
        IPAddress.getText();
        IPAddress.setText(linMed.getIP());
        onlineLabel.setText("OFFLINE");
        portField.setText("5900");
        configuration.setPort(5900);
        server.setConfig(configuration);
        passwordField.setText(server.getConfig().getPassword());
        passwordLabel.setText(server.getConfig().getPassword());

        sharedLabel.setText("");
        SSHLabel.setText("");
        logVNCActivityButton.setText("Log VNC Activity");
    }

    @Override
    public void closeView()
    {
        server.stop();
    }

    @Override
    public void generateScript(ScriptWriter writer)
    {

    }

}
