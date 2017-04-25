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

    private static final String START_KEY = "startserver";
    private static final String STOP_KEY = "stopserver";
    private static final String SHARECONNECTION = "shareConnection";
    private static final String SSH = "SSH";
    private static final String DEFAULT_VALUE = "VNC Server foo";
    private static final String PASSWORD_FIELD = "passwordField";
    private static final String PORT_FIELD = "portText";

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

    @Input(id = START_KEY)
    private InputButton startButton;

    @Input(id = SSH)
    private InputButton sshButton;

    @Input(id = STOP_KEY)
    private InputButton stopButton;

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

    @Input(id = START_KEY)
    public void onStartClick(InputEvent event)
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
            configuration.persist(model);
        }
    }

    @Input(id = STOP_KEY)
    public void onStopClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            server.stop();
            onlineLabel.setText("OFFLINE");
        }
    }

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

        configuration = Configuration.createConfiguration(model);

        startButton.setText("Start");
        stopButton.setText("Stop");
        shareConnection.setVisible(false);
        sshButton.setVisible(false);
        portLabel.setVisible(false);
        sharedLabel.setVisible(false);
        SSHLabel.setVisible(false);
        passwordLabel.setText(configuration.getPassword());
        IPAddress.getText();
        IPAddress.setText(linMed.getIP());
        onlineLabel.setText("OFFLINE");
        portField.setText("5900");
        configuration.setPort(5900);
        passwordField.setText(Configuration.DEFAULT_PASSWORD);
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
