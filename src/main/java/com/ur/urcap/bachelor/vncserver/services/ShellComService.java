/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.services;

import com.ur.urcap.bachelor.vncserver.exceptions.UnsuccessfulCommandException;

/**
 *
 * @author frede
 */
public interface ShellComService
{

    ShellCommandResponse doCommand(String command, String[] answers) throws UnsuccessfulCommandException;
    
    ShellCommandResponse doCommand(String command) throws UnsuccessfulCommandException;

    ShellCommandResponse  runScript(String path) throws UnsuccessfulCommandException;
}
