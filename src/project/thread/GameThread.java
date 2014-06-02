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
import project.card.RegCard;
import project.lima.Player;
import project.pad.*;

public class GameThread extends Thread
{
	GamePad gamepad;
	ControlPad controlpad;
	ChatPad chatpad;
	
	/**
	 *
	 * @param cardpad
	 * @param controlpad
	 */
	public GameThread(GamePad cardpad, ControlPad controlpad, ChatPad chatpad)
	{
		this.controlpad=controlpad;
		this.gamepad = cardpad;
		this.chatpad = chatpad;
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
			if (name.equals(GamePad.yourName))
			{
				GamePad.userPlayer.setNewCredits(Integer.parseInt(credits));
				GamePad.userPlayer.setWcp(Integer.parseInt(wcp));
				GamePad.userPlayer.setName(name);
				//parse deck
				String numDealt = data[3];
				GamePad.userPlayer.setDeckND(Integer.parseInt(numDealt));
				String isFirst = data[4];
				if (isFirst.equals("true"))
					GamePad.userPlayer.setIsFirst(true);
				else
					GamePad.userPlayer.setIsFirst(false);
				GamePad.userPlayer.clearHand();
				//hand
				String[] hand = data[1].split(";");
				for (int i = 0 ; i < hand.length; i ++)
				{
					Card temp;
					String[] card = hand[i].split(":");
					if (!card[0].equals(""))
					{
						if (card[0].equals("ability"))
							temp = new AbilityCard(card[1], Integer.parseInt(card[2]), card[3], card[4], ability.valueOf(card[5]));
						else
							temp = new RegCard(card[1], Integer.parseInt(card[2]), card[3], Integer.parseInt(card[4]), Integer.parseInt(card[5]), card[6], ability.valueOf(card[7]), Integer.parseInt(card[8]), Integer.parseInt(card[9]));
						GamePad.userPlayer.addCardToHand(temp);
					}
				}
				//board
				GamePad.userPlayer.clearBoard();
				String[] board = data[2].split(";");
				for (int i = 0 ; i < board.length; i ++)
				{
					Card temp;
					String[] card = board[i].split(":");
					if (!card[0].equals(""))
					{
						temp = new RegCard(card[1], Integer.parseInt(card[2]), card[3], Integer.parseInt(card[4]), Integer.parseInt(card[5]), card[6], ability.valueOf(card[7]), Integer.parseInt(card[8]), Integer.parseInt(card[9]));
						GamePad.userPlayer.addCardtoBoard(temp);
					}
				}
			}
			else
			{
				GamePad.enemyPlayer.setNewCredits(Integer.parseInt(credits));
				GamePad.enemyPlayer.setWcp(Integer.parseInt(wcp));
				GamePad.enemyPlayer.setName(name);
				//parse deck
				String numDealt = data[3];
				GamePad.enemyPlayer.setDeckND(Integer.parseInt(numDealt));
				String isFirst = data[4];
				if (isFirst.equals("true"))
					GamePad.userPlayer.setIsFirst(true);
				else
					GamePad.userPlayer.setIsFirst(false);				
				GamePad.enemyPlayer.clearHand();
				//hand
				String[] hand = data[1].split(";");
				for (int i = 0 ; i < hand.length; i ++)
				{
					Card temp;
					String[] card = hand[i].split(":");
					if (!card[0].equals(""))
					{
						if (card[0].equals("ability"))
							temp = new AbilityCard(card[1], Integer.parseInt(card[2]), card[3], card[4], ability.valueOf(card[5]));
						else
							temp = new RegCard(card[1], Integer.parseInt(card[2]), card[3], Integer.parseInt(card[4]), Integer.parseInt(card[5]), card[6], ability.valueOf(card[7]), Integer.parseInt(card[8]), Integer.parseInt(card[9]));
						GamePad.enemyPlayer.addCardToHand(temp);
					}
				}
				//board
				GamePad.enemyPlayer.clearBoard();
				String[] board = data[2].split(";");
				for (int i = 0 ; i < board.length; i ++)
				{
					Card temp;
					String[] card = board[i].split(":");
					if (!card[0].equals(""))
					{
						temp = new RegCard(card[1], Integer.parseInt(card[2]), card[3], Integer.parseInt(card[4]), Integer.parseInt(card[5]), card[6], ability.valueOf(card[7]), Integer.parseInt(card[8]), Integer.parseInt(card[9]));
						GamePad.enemyPlayer.addCardtoBoard(temp);
					}
				}
				
			}
			gamepad.updateBoardCards();
			gamepad.updateHandCards();
			gamepad.updatePlayerStats();
			if (GamePad.userPlayer.getCredits() <= 0)
			{
				chatpad.chatLineArea.append("Game>Game Over, You have lost\n");
				chatpad.chatLineArea.setCaretPosition(chatpad.chatLineArea.getText().length());
				gamepad.setEndOfTurn(true);
			}
			else if (GamePad.enemyPlayer.getCredits() <= 0)
			{
				chatpad.chatLineArea.append("Game>Game Over, You have won!\n");
				chatpad.chatLineArea.setCaretPosition(chatpad.chatLineArea.getText().length());
				gamepad.setEndOfTurn(true);				
			}
		}
		else if(recMessage.startsWith("/endTurn "))
		{
			recMessage = recMessage.substring(9);
			//create game message alerting users of endturn
			String[] user = recMessage.split("::");
			chatpad.chatLineArea.append("Game>" + user[0] + " is finished with their turn \n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
			chatpad.chatLineArea.append("Game>it is now your turn\n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
			gamepad.setEndOfTurn(false);
			controlpad.endTurnButton.setEnabled(true);
			GamePad.userPlayer.draw();
			GamePad.userPlayer.setWcp(5);
			gamepad.updateBoardCards();
			gamepad.updateHandCards();
			gamepad.updatePlayerStats();
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
			System.out.println(recMessage);
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

