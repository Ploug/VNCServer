/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ur.urcap.bachelor.vncserver.exceptions;

/**
 *
 * @author frede
 */
public class UnsuccessfulCommandException extends Exception {
  public UnsuccessfulCommandException() { super(); }
  public UnsuccessfulCommandException(String message) { super(message); }
  public UnsuccessfulCommandException(String message, Throwable cause) { super(message, cause); }
  public UnsuccessfulCommandException(Throwable cause) { super(cause); }
}
