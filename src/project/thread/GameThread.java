/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.thread;
import java.io.*;
import java.util.*;
import project.pad.*;

public class GameThread extends Thread
{
 GamePad gamepad;
 ControlPad controlpad;

 public GameThread(GamePad cardpad, ControlPad controlpad)
 {
  this.controlpad=controlpad;
  this.gamepad = cardpad;
 }

 public void sendMessage(String sndMessage)
 {
  try
  {
   gamepad.outData.writeUTF(sndMessage);
  }
  catch(Exception ea)
  {
   System.out.println("GameThread.sendMessage:"+ea);
  }
 }


 public void acceptMessage(String recMessage)
 {
     //TODO you received message from the peer, so you should update the his/her cards on the board
  if(recMessage.startsWith("/chess "))
  {
      //For exampl, gamepad.repaint()
      System.out.println("you receive:"+recMessage);
      controlpad.statusText.setText("recMessage");
  }
  else if(recMessage.startsWith("/yourname "))
  {
   gamepad.selfName=recMessage.substring(10);
  }
  else if(recMessage.equals("/error"))
  {
   controlpad.statusText.setText("Error: User not exist! Please restart the client.");
  }
  else
  {
   //System.out.println(recMessage);
  }
 }


 public void run()
 {
     String message="";
     try
     {
   while(true)
   {
    message=gamepad.inData.readUTF();
    acceptMessage(message);
   }
  }
  catch(IOException es)
  {
  }
 }

}

 