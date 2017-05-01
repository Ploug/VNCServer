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
import com.ur.urcap.bachelor.vncserver.business.Configuration;
import com.ur.urcap.bachelor.vncserver.business.VNCServer;
import com.ur.urcap.bachelor.vncserver.business.shell.LinuxMediator;
import com.ur.urcap.bachelor.vncserver.exceptions.UnsuccessfulCommandException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VNCServerInstallationNodeContribution implements InstallationNodeContribution
{

    private static final String STARTSTOP_KEY = "startStopServer";
    private static final String SHARECONNECTION = "shareConnection";
    private static final String DEFAULT_VALUE = "VNC Server foo";
    private static final String PASSWORD_FIELD = "passwordField";
    private static final String PORT_FIELD = "portText";
    private static final String LOGVNCACTIVITY = "logVNCActivity";
    private static final String LOG_PATH = "logPath";
    private static final String INSTALL_VNC_TEXT = "installVNCText";
    private final String folderName = "vncGui";

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

    @Input(id = SHARECONNECTION)
    private InputButton shareConnectionButton;

    @Label(id = "ip")
    private LabelComponent IPAddress;

    @Label(id = PASSWORRD_LABEL)
    private LabelComponent passwordLabel;

    @Label(id = LOG_PATH)
    private LabelComponent logPath;

    @Label(id = INSTALL_VNC_TEXT)
    private LabelComponent installLabel;

    @Label(id = "Online")
    private LabelComponent onlineLabel;

    @Label(id = "portLabel")
    private LabelComponent portLabel;

    @Label(id = "errorText")
    private LabelComponent errorLabel;

    @Label(id = "passwordInfo")
    private LabelComponent passwordInfoLabel;

    @Label(id = "logLabel")
    private LabelComponent logLabel;

    @Label(id = "Shared")
    private LabelComponent sharedLabel;

    @Input(id = PASSWORD_FIELD)
    private InputTextField passwordField;

    @Input(id = PORT_FIELD)
    private InputTextField portField;

    private boolean neededProgramsInstalled;

    @Input(id = STARTSTOP_KEY)
    public void onStartStopClick(InputEvent event)
    {

        if (event.getEventType() == InputEvent.EventType.ON_PRESSED && neededProgramsInstalled)
        {
            try
            {
                if (!server.isActive())
                {

                    server.start();
                    onlineLabel.setText("ONLINE");
                    passwordLabel.setText(server.getConfig().getPassword());
                    portLabel.setText(server.getConfig().getPort() + "");
                    sharedLabel.setText(server.getConfig().isShared() + "");
                    logLabel.setText(server.getConfig().isLogging() + "");
                    startStopButton.setText("Stop");
                }
                else
                {
                    server.stop();
                    onlineLabel.setText("OFFLINE");
                    passwordLabel.setText("");
                    portLabel.setText("");
                    sharedLabel.setText("");
                    logLabel.setText("");
                    startStopButton.setText("Start");
                }
                errorLabel.setText("");
            }
            catch (UnsuccessfulCommandException ex)
            {
                errorLabel.setText(ex.getMessage());
            }
        }
    }

    @Input(id = LOGVNCACTIVITY)
    public void onLogVNCActivityClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            configuration.setLogging(!configuration.isLogging());
            logVNCActivityButton.setText("Log VNC Activity: " + configuration.isLogging());
            configuration.persist(model);
        }
    }

    @Input(id = PASSWORD_FIELD)
    public void onPasswordFieldEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            String suggestedPassword = passwordField.getText();
            if (suggestedPassword.length() > 0 && suggestedPassword.length() < 50)
            {
                configuration.setPassword(suggestedPassword);
                if (configuration.getPassword().equals(Configuration.DEFAULT_PASSWORD))
                {
                    passwordInfoLabel.setText("WARNING: You are using the default password");
                }
                else
                {
                    passwordInfoLabel.setText("");
                }
            }
            else
            {
                passwordField.setText(configuration.getPassword());

            }
            configuration.persist(model);
        }
    }

    @Input(id = PORT_FIELD)
    public void onPortFieldEnter(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_CHANGE)
        {
            try
            {
                int port = Integer.parseInt(portField.getText());

                configuration.setPort(port);

            }

            catch (NumberFormatException ex)
            {
                portField.setText(configuration.getPort() + "");
            }
            configuration.persist(model);

        }
    }

    @Input(id = SHARECONNECTION)
    public void onShareConnectionClick(InputEvent event)
    {
        if (event.getEventType() == InputEvent.EventType.ON_PRESSED)
        {
            configuration.setShared(!configuration.isShared());
            shareConnectionButton.setText("Shared: " + configuration.isShared());
            configuration.persist(model);
        }
    }

    @Override
    public void openView()
    {

        server = VNCServer.createServer(model);
        linMed = new LinuxMediator(folderName);

        installLabel.setText("");

        Thread installMissing = new Thread(new Runnable() // Installing missing programs asynchronously
        {
            @Override
            public void run()
            {

                try
                {
                    synchronized (installLabel)
                    {
                        installLabel.setText("Installing missing programs...");
                    }

                    synchronized (linMed)
                    {
                        neededProgramsInstalled = linMed.installMissingPrograms();
                    }

                    synchronized (installLabel)
                    {
                        installLabel.setText("Missing programs were installed correctly!");
                    }
                    Thread.sleep(10000);

                    synchronized (installLabel)
                    {
                        installLabel.setText("");
                    }

                }
                catch (UnsuccessfulCommandException ex)
                {
                    synchronized (installLabel)
                    {
                        installLabel.setText("Could not install missing programs. Something wrong with the network or OS access?");
                    }
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(VNCServerInstallationNodeContribution.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        if (!linMed.neededProgramsInstalled())
        {
            installMissing.start();
                    
        }
        else
        {
            neededProgramsInstalled = true;
        }

        startStopButton.setText(
                "Start");
        shareConnectionButton.setText(
                "Share connection");
        errorLabel.setText(
                "");
        configuration = server.getConfig();

        logPath.setText(
                "Logs saved at: " + server.getDataPath());
        IPAddress.setText(linMed.getIP());
        if (server.isActive())
        {
            onlineLabel.setText("ONLINE");
            passwordLabel.setText(server.getConfig().getPassword());
            portLabel.setText(server.getConfig().getPort() + "");
            sharedLabel.setText(server.getConfig().isShared() + "");
            logLabel.setText(server.getConfig().isLogging() + "");
            startStopButton.setText("Stop");
        }

        else
        {
            onlineLabel.setText("OFFLINE");
            passwordLabel.setText("");
            portLabel.setText("");
            sharedLabel.setText("");
            logLabel.setText("");
            startStopButton.setText("Start");
        }

        if (configuration.getPassword()
                .equals(Configuration.DEFAULT_PASSWORD))
        {
            passwordInfoLabel.setText("WARNING: You are using the default password");
        }

        else
        {
            passwordInfoLabel.setText("");
        }

        server.setConfig(configuration);

        passwordField.setText(configuration.getPassword());
        portField.setText(configuration.getPort() + "");
        shareConnectionButton.setText(
                "Shared: " + configuration.isShared());
        logVNCActivityButton.setText(
                "Log VNC Activity: " + configuration.isLogging());
    }

    @Override
    public void closeView()
    {
        server.persist(model);
    }

    @Override
    public void generateScript(ScriptWriter writer)
    {

    }

}
