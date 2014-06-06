package project.thread;

import project.lima.*;
import project.pad.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Handles incoming messages from other players.
 * @author Team Lima
 */
public class ClientThread extends Thread
{
	ProjectLima gameclient;
	
	/**
	 * Constructs new client thread.
	 * @param chessclient The client
	 */
	public ClientThread(ProjectLima chessclient)
	{
		this.gameclient=chessclient;
	}
	
	/**
	 * Accept message from server.
	 * @param recMessage the message from the server
	 */
	public void acceptMessage(String recMessage)
	{
		if(recMessage.startsWith("/userlist "))
		{
			StringTokenizer userToken=new StringTokenizer(recMessage," ");
			int userNumber=0;
			
			gameclient.userpad.userList.removeAll();
			gameclient.inputpad.userChoice.removeAll();
			gameclient.inputpad.userChoice.addItem("All");
			while(userToken.hasMoreTokens())
			{
				String user=(String)userToken.nextToken(" ");
				if(userNumber>0 && !user.startsWith("[inchess]"))
				{
					gameclient.userpad.userList.add(user);
					gameclient.inputpad.userChoice.addItem(user);
				}
				
				userNumber++;
			}
			gameclient.inputpad.userChoice.select("All");
		}
		else if(recMessage.startsWith("/yourname "))
		{
			gameclient.gameClientName=recMessage.substring(10);
			gameclient.setTitle("Java....Game Client "+"User Name:"+gameclient.gameClientName);
		}
		else if(recMessage.equals("/reject"))
		{
			try
			{
				gameclient.controlpad.statusText.setText("Can not join the game.");
				gameclient.controlpad.cancelGameButton.setEnabled(false);
				gameclient.controlpad.joinGameButton.setEnabled(true);
				gameclient.controlpad.creatGameButton.setEnabled(true);
			}
			catch(Exception ef)
			{
				gameclient.chatpad.chatLineArea.setText("chessclient.chesspad.chessSocket.close Can not close");
			}
			gameclient.controlpad.joinGameButton.setEnabled(true);
		}
		else if(recMessage.startsWith("/peer "))
		{
			gameclient.gamepad.peerName=recMessage.substring(6);
			if(gameclient.isServer)
			{
				
				//gameclient.gamepad.isMouseEnabled=true;
				gameclient.controlpad.statusText.setText("Please play a card.");
				gameclient.controlpad.readyButton.setEnabled(true);
			}
			else if(gameclient.isClient)
			{
				//gameclient.gamepad.chessColor=-1;
				gameclient.controlpad.statusText.setText("Joined, wait the opponent...");
				gameclient.controlpad.readyButton.setEnabled(true);
			}
			
		}
		else if(recMessage.equals("/youwin"))
		{
			if (gameclient.isOnGame == true)
			{
				gameclient.isOnGame=false;
				//gameclient.gamepad.chessVictory(gameclient.gamepad.chessColor);
				if (gameclient.gamepad.endGame == false)
				{
					gameclient.chatpad.chatLineArea.append("Game>Opponent quit, you have won the game!\n");
					gameclient.chatpad.chatLineArea.setCaretPosition(
							gameclient.chatpad.chatLineArea.getText().length());
				}
			}
			
		}
		else if(recMessage.equals("/OK"))
		{
			gameclient.controlpad.statusText.setText("Game created, wait others join in...");
		}
		else if(recMessage.equals("/error"))
		{
			gameclient.chatpad.chatLineArea.append("Error, please quit \n");
		}
		else
		{
			gameclient.chatpad.chatLineArea.append(recMessage+"\n");
			gameclient.chatpad.chatLineArea.setCaretPosition(
					gameclient.chatpad.chatLineArea.getText().length());
		}
	}
	
	
	public void run()
	{
		String message="";
		try
		{
			while(true)
			{
				message=gameclient.in.readUTF();
				acceptMessage(message);
			}
		}
		catch(IOException es)
		{
		}
	}
	
}