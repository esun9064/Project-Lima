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

/**
 * Thread handles messages related to the game.
 * @author Team Lima
 */
public class GameThread extends Thread
{
	GamePad gamepad;
	ControlPad controlpad;
	ChatPad chatpad;
	
	/**
	 * Constructs new game thread.
	 * @param cardpad board interface
	 * @param controlpad bottom menu interface
	 */
	public GameThread(GamePad cardpad, ControlPad controlpad, ChatPad chatpad)
	{
		this.controlpad=controlpad;
		this.gamepad = cardpad;
		this.chatpad = chatpad;
	}
	
	/**
	 * Send message to server.
	 * @param sndMessage message to be sent
	 */
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
	
	/**
	 * Accept a message from the server.
	 * @param recMessage message received
	 */
	public void acceptMessage(String recMessage)
	{
		//recieved serialized player objects
		if(recMessage.startsWith("/chess "))
		{
			//parse order
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
			if (name.equals(gamepad.yourName))
			{
				gamepad.userPlayer.setNewCredits(Integer.parseInt(credits));
				gamepad.userPlayer.setWcp(Integer.parseInt(wcp));
				gamepad.userPlayer.setName(name);
				//parse deck
				String numDealt = data[3];
				gamepad.userPlayer.setDeckND(Integer.parseInt(numDealt));
				gamepad.userPlayer.clearHand();
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
						gamepad.userPlayer.addCardToHand(temp);
					}
				}
				//board
				gamepad.userPlayer.clearBoard();
				String[] board = data[2].split(";");
				for (int i = 0 ; i < board.length; i ++)
				{
					Card temp;
					String[] card = board[i].split(":");
					if (!card[0].equals(""))
					{
						temp = new RegCard(card[1], Integer.parseInt(card[2]), card[3], Integer.parseInt(card[4]), Integer.parseInt(card[5]), card[6], ability.valueOf(card[7]), Integer.parseInt(card[8]), Integer.parseInt(card[9]));
						gamepad.userPlayer.addCardtoBoard(temp);
					}
				}
			}
			else
			{
				gamepad.enemyPlayer.setNewCredits(Integer.parseInt(credits));
				gamepad.enemyPlayer.setWcp(Integer.parseInt(wcp));
				gamepad.enemyPlayer.setName(name);
				//parse deck
				String numDealt = data[3];
				gamepad.enemyPlayer.setDeckND(Integer.parseInt(numDealt));
				gamepad.enemyPlayer.clearHand();
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
						gamepad.enemyPlayer.addCardToHand(temp);
					}
				}
				//board
				gamepad.enemyPlayer.clearBoard();
				String[] board = data[2].split(";");
				for (int i = 0 ; i < board.length; i ++)
				{
					Card temp;
					String[] card = board[i].split(":");
					if (!card[0].equals(""))
					{
						temp = new RegCard(card[1], Integer.parseInt(card[2]), card[3], Integer.parseInt(card[4]), Integer.parseInt(card[5]), card[6], ability.valueOf(card[7]), Integer.parseInt(card[8]), Integer.parseInt(card[9]));
						gamepad.enemyPlayer.addCardtoBoard(temp);
					}
				}
				
			}
			gamepad.updateBoardCards();
			gamepad.updateHandCards();
			gamepad.updatePlayerStats();
			gamepad.revalidate();
			gamepad.repaint();
		}
		//receive message about end of turn
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
			gamepad.userPlayer.draw();
			gamepad.userPlayer.setWcp(5);
			gamepad.updateBoardCards();
			gamepad.updateHandCards();
			gamepad.updatePlayerStats();
			gamepad.revalidate();
			gamepad.repaint();
		}
		//notifications about what opponent is doing
		else if (recMessage.startsWith("/gameMsg "))
		{
			recMessage = recMessage.substring(9);
			chatpad.chatLineArea.append("Game>" + recMessage + "\n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
		}
		else if(recMessage.startsWith("/yourname "))
		{
			gamepad.selfName=recMessage.substring(10);
		}
		else if (recMessage.startsWith("/giveup "))
		{
			chatpad.chatLineArea.append("Game>" + gamepad.peerName.substring(9) + " has quit, You have won the game!\n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
		}
		/**
		else if (recMessage.startsWith("/opponentquit "))
		{
			chatpad.chatLineArea.append("Game>" + gamepad.peerName.substring(9) + " has quit, You have won the game!\n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
		}	
		*/
		else if (recMessage.startsWith("/youwin "))
		{
			gamepad.endGame = true;
			chatpad.chatLineArea.append("Game>" + gamepad.peerName.substring(9) + " has been defeated! You win!\n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
		}			
		else if (recMessage.startsWith("/youlost "))
		{
			gamepad.endGame = true;
			chatpad.chatLineArea.append("Game>" + gamepad.actualName + " has been defeated! You Lost!\n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
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

