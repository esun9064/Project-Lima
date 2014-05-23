/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.thread;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import project.pad.*;

public class CardThread extends Thread
{
 CardPad cardpad;

 public CardThread(CardPad cardpad)
 {
  this.cardpad=cardpad;
 }

 public void sendMessage(String sndMessage)
 {
  try
  {
   cardpad.outData.writeUTF(sndMessage);
  }
  catch(Exception ea)
  {
   System.out.println("chessThread.sendMessage:"+ea);
  }
 }


 public void acceptMessage(String recMessage)
 {
  if(recMessage.startsWith("/chess "))
  {
   StringTokenizer userToken=new StringTokenizer(recMessage," ");
   String chessToken;
   String[] chessOpt={"-1","-1","0"};
   int chessOptNum=0;

   while(userToken.hasMoreTokens())
   {
    chessToken=(String)userToken.nextToken(" ");
    if(chessOptNum>=1 && chessOptNum<=3)
    {
     chessOpt[chessOptNum-1]=chessToken;

    }
    chessOptNum++;
   }
   cardpad.netChessPaint(Integer.parseInt(chessOpt[0]),Integer.parseInt(chessOpt[1]),Integer.parseInt(chessOpt[2]));

  }
  else if(recMessage.startsWith("/yourname "))
  {
   cardpad.selfName=recMessage.substring(10);
  }
  else if(recMessage.equals("/error"))
  {
   cardpad.statusText.setText("Error: User not exist! Please restart the client.");
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
    message=cardpad.inData.readUTF();
    acceptMessage(message);
   }
  }
  catch(IOException es)
  {
  }
 }

}

 