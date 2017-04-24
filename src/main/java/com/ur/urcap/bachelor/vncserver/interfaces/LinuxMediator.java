/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.interfaces;

import com.ur.urcap.bachelor.vncserver.business.shell.UnsuccessfulCommandException;
import java.io.File;

/**
 *
 * @author frede
 */
public interface LinuxMediator
{

    void installVNC() throws UnsuccessfulCommandException;

    void setVNCPassword(String password) throws UnsuccessfulCommandException;

    void startVNC(boolean shared, int port, boolean ssh, boolean log) throws UnsuccessfulCommandException;

    void stopVNC() throws UnsuccessfulCommandException;
    String getIP();
}
