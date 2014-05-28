/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.thread;
import java.io.*;
import java.util.*;
import project.card.AbilityCard;
import project.card.Card;
import project.card.Card.ability;
import project.lima.Player;
import project.pad.*;

public class GameThread extends Thread
{
 GamePad gamepad;
 ControlPad controlpad;

 /**
  * 
  * @param cardpad
  * @param controlpad 
  */
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
  if(recMessage.startsWith("/chess "))
  {

	//name; credits; wcp
	//abilitycard name, cost, image, description, ability
	//regcard, name, cost, image, attack, healt, ability, maxattack, maxability
	  
	  
	//0 player attributes
	//1 hand
	//2 board
	//3 deck
	recMessage = recMessage.substring(7);
	String[] data = recMessage.split("\n");
	String[] player = data[0].split(";");
	//parse player attributes
	String name = player[0];
	String credits = player[1];
	String wcp = player[2];
	GamePad.userPlayer.setNewCredits(Integer.parseInt(credits));
	GamePad.userPlayer.setWcp(Integer.parseInt(wcp));
	GamePad.userPlayer.setName(name);
	//parse deck
	String numDealt = data[3];
	GamePad.userPlayer.setDeckND(Integer.parseInt(numDealt));
	GamePad.userPlayer.clearHand();
	//hand
	String[] hand = data[1].split(";");
	for (int i = 0 ; i < hand.length; i ++)
	{
		Card temp;
		String[] card = hand[i].split(",");
		if (card[0].equals("ability"))
			temp = new AbilityCard(card[0], Integer.parseInt(card[1]), card[2], card[3], ability.valueOf(card[4]));
		if (card[0].equals("reg"))
			temp = new RegCard(card[0], Integer.parseInt(card[1], card[2], card[3], card[4], card[5], card[6], card[7], card[8]));
	}
	
	
	
	
	  

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

 