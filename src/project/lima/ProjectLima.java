package project.lima;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import project.card.*;
import project.card.Card.ability;
import project.deck.*;
import project.pad.*;
import project.thread.*;

/**
 * Main class, used in game. Provides overall frame of application.
 * @author Team Lima
 */
public class ProjectLima extends JFrame implements ActionListener,KeyListener
{
	
	public UserPad userpad=new UserPad();
	public ChatPad chatpad=new ChatPad();
	public ControlPad controlpad=new ControlPad();
	public GamePad gamepad=new GamePad(controlpad, chatpad);
	public InputPad inputpad=new InputPad();
	
	
	public Socket chatSocket;
	public DataInputStream in;
	public DataOutputStream out;
	public String gameClientName=null;
	public String host=null;
	public int port=4331;
	
	boolean isOnChat=false;  //�����죿
	public boolean isOnGame=false; //�����壿
	boolean isGameConnected=false; //����Ŀͻ������ӣ�
	public boolean isServer=false; //��������������
	public boolean isClient=false; //���������Ŀͻ���
	
	Panel southPanel=new Panel();
	Panel northPanel=new Panel();
	//Panel centerPanel=new Panel();
	Panel westPanel=new Panel();
	Panel eastPanel=new Panel();
	
	//overall menubar
	public static JMenuBar gameMenu = new JMenuBar();
	public static JMenu file = new JMenu("File");
	public static JMenuItem quit = new JMenuItem("Quit");
	
	/**
	 * Construct new client, adds all interface components together into the frame.
	 */
	ProjectLima()
	{
		super("Java ...Game Client");
		setLayout(new BorderLayout());
		host=controlpad.inputIP.getText();
		
		westPanel.setLayout(new BorderLayout());
		westPanel.add(userpad,BorderLayout.NORTH);
		westPanel.add(chatpad,BorderLayout.CENTER);
		westPanel.add(inputpad,BorderLayout.SOUTH);
		westPanel.setBackground(Color.pink);
		
		inputpad.inputWords.addKeyListener(this);
		gamepad.host=controlpad.inputIP.getText();
		
		//centerPanel.add(gamepad,BorderLayout.CENTER);
		//centerPanel.add(inputpad,BorderLayout.SOUTH);
		//centerPanel.setBackground(Color.pink);
		
		controlpad.connectButton.addActionListener(this);
		controlpad.creatGameButton.addActionListener(this);
		controlpad.joinGameButton.addActionListener(this);
		controlpad.cancelGameButton.addActionListener(this);
		controlpad.readyButton.addActionListener(this);
		controlpad.endTurnButton.addActionListener(this);
		controlpad.exitGameButton.addActionListener(this);
		
		controlpad.creatGameButton.setEnabled(false);
		controlpad.joinGameButton.setEnabled(false);
		controlpad.cancelGameButton.setEnabled(false);
		
		southPanel.add(controlpad,BorderLayout.CENTER);
		//southPanel.setBackground(Color.pink);
		
		this.setPreferredSize(new Dimension(1366, 720));
		this.setResizable(true);
		this.pack();
		this.revalidate();
		this.repaint();
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if(isOnChat)
				{
					try
					{
						chatSocket.close();
					}
					catch(Exception ed)
					{
					}
				}
				if(isOnGame || isGameConnected)
				{
					try
					{
						gamepad.gameSocket.close();
					}
					catch(Exception ee)
					{
					}
				}
				System.exit(0);
			}
			public void windowActivated(WindowEvent ea)
			{
				
			}
		});
		
		add(westPanel,BorderLayout.WEST);
		add(gamepad,BorderLayout.CENTER);
		add(southPanel,BorderLayout.SOUTH);
		
		pack();
		setSize(1366,720);
		setVisible(true);
		setResizable(false);
		//add main menu items
		file.add(quit);
		gameMenu.add(file);
		
		validate();
	}
	
	/**
	 * 
	 * @param serverIP server's IP address
	 * @param serverPort port number
	 * @return true if connected, false if failed
	 * @throws Exception
	 */
	public boolean connectServer(String serverIP,int serverPort) throws Exception
	{
		try
		{
			chatSocket=new Socket(serverIP,serverPort);
			in=new DataInputStream(chatSocket.getInputStream());
			out=new DataOutputStream(chatSocket.getOutputStream());
			
			ClientThread clientthread=new ClientThread(this);
			clientthread.start();
			isOnChat=true;
			return true;
		}
		catch(IOException ex)
		{
			chatpad.chatLineArea.setText("chessClient:connectServer: Connect failed, please restart the client \n");
		}
		return false;
	}
	
	/**
	 * Handles button click events.
	 * @param e the ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==controlpad.connectButton)
		{
			host=gamepad.host=controlpad.inputIP.getText();
			try
			{
				if(connectServer(host,port))
				{
					chatpad.chatLineArea.setText("");
					controlpad.connectButton.setEnabled(false);
					controlpad.creatGameButton.setEnabled(true);
					controlpad.joinGameButton.setEnabled(true);
					controlpad.statusText.setText("Connected, please create or join a game");
					out.writeUTF("/changename " + gamepad.actualName);
				}
				
				
			}
			catch(Exception ei)
			{
				chatpad.chatLineArea.setText("controlpad.connectButton:Connect failed, please restart the client \n");
			}
		}
		else if(e.getSource()==controlpad.exitGameButton)
		{
			if(isOnChat)
			{
				try
				{
					chatSocket.close();
				}
				catch(Exception ed)
				{
				}
			}
			if(isOnGame || isGameConnected)
			{
				try
				{
					gamepad.gameSocket.close();
				}
				catch(Exception ee)
				{
				}
			}
			System.exit(0);
			
		}else if(e.getSource()==controlpad.joinGameButton)  {
			String selectedUser=userpad.userList.getSelectedItem();
			if(selectedUser==null || selectedUser.startsWith("[inchess]") ||
					selectedUser.equals(gameClientName))
			{
				controlpad.statusText.setText("Please select a user...");
			}
			else
			{
				try
				{
					if(!isGameConnected)
					{
						if(gamepad.connectServer(gamepad.host,gamepad.port))
						{
							isGameConnected=true;
							isOnGame=true;
							isClient=true;
							controlpad.creatGameButton.setEnabled(false);
							controlpad.joinGameButton.setEnabled(false);
							controlpad.cancelGameButton.setEnabled(true);
							gamepad.gamethread.sendMessage("/joingame "+userpad.userList.getSelectedItem()+" "+gameClientName);
							out.writeUTF("/" + gamepad.actualName + " joining game, please wait a moment");
							
							gamepad.initCards();
							gamepad.showBoard();
							out.writeUTF("/" + gamepad.peerName.substring(9) + " has joined the game, \n please click \"ready to play\".");
						}
					}
					else
					{
						isOnGame=true;
						isClient=true;
						controlpad.creatGameButton.setEnabled(false);
						controlpad.joinGameButton.setEnabled(false);
						controlpad.cancelGameButton.setEnabled(true);
						gamepad.gamethread.sendMessage("/joingame "+userpad.userList.getSelectedItem()+" "+gameClientName);
						out.writeUTF("/" + gamepad.actualName + " joining game, please wait a moment");
						
						gamepad.initCards();
						gamepad.showBoard();
						out.writeUTF("/" + gamepad.peerName.substring(9) + " has joined the game, \n please click \"ready to play\".");
					}
					
					
				}
				catch(Exception ee)
				{
					isGameConnected=false;
					isOnGame=false;
					isClient=false;
					controlpad.creatGameButton.setEnabled(true);
					controlpad.joinGameButton.setEnabled(true);
					controlpad.cancelGameButton.setEnabled(false);
					chatpad.chatLineArea.setText("gamepad.connectServer Connect failed \n"+ee);
				}
				
			}
		}else if(e.getSource()==controlpad.creatGameButton) {
			try
			{
				if(!isGameConnected)
				{
					if(gamepad.connectServer(gamepad.host,gamepad.port))
					{
						
						isGameConnected=true;
						isOnGame=true;
						isServer=true;
						controlpad.creatGameButton.setEnabled(false);
						controlpad.joinGameButton.setEnabled(false);
						controlpad.cancelGameButton.setEnabled(true);
						gamepad.gamethread.sendMessage("/creatgame "+"[inchess]"+gameClientName);
						controlpad.endTurnButton.setEnabled(true);
						out.writeUTF("/" + gamepad.actualName + " creating game, please wait a moment.");
						gamepad.initCards();
						gamepad.showBoard();
						chatpad.chatLineArea.append("Game>Game created\n");
						chatpad.chatLineArea.setCaretPosition(
								chatpad.chatLineArea.getText().length());			//gameclient.gamepad.isMouseEnabled=false;
						gamepad.setEndOfTurn(false);
					}
				}
				else
				{
					isOnGame=true;
					isServer=true;
					out.writeUTF("/" + gamepad.actualName + " creating game, please wait a moment.");
					controlpad.creatGameButton.setEnabled(false);
					controlpad.joinGameButton.setEnabled(false);
					controlpad.cancelGameButton.setEnabled(true);
					controlpad.endTurnButton.setEnabled(true);
					gamepad.setEndOfTurn(false);
					gamepad.initCards();
					gamepad.showBoard();
					chatpad.chatLineArea.append("Game>Game created\n");
					chatpad.chatLineArea.setCaretPosition(
							chatpad.chatLineArea.getText().length());			//gameclient.gamepad.isMouseEnabled=false;

					gamepad.gamethread.sendMessage("/creatgame "+"[inchess]"+gameClientName);
				}
			}
			catch(Exception ec)
			{
				isGameConnected=false;
				isOnGame=false;
				isServer=false;
				controlpad.creatGameButton.setEnabled(true);
				controlpad.joinGameButton.setEnabled(true);
				controlpad.cancelGameButton.setEnabled(false);
				ec.printStackTrace();
				chatpad.chatLineArea.setText("gamepad.connectServer Connect failed \n"+ec);
			}
			
		}else if(e.getSource()==controlpad.cancelGameButton){
			if(isOnGame)
			{
				gamepad.gamethread.sendMessage("/giveup "+gameClientName);
				if (gamepad.endGame == false)
					gamepad.gamethread.sendMessage("/" + gamepad.peerName+ " /opponentquit " + gamepad.yourName);
				
				
				controlpad.creatGameButton.setEnabled(true);
				controlpad.joinGameButton.setEnabled(true);
				controlpad.cancelGameButton.setEnabled(false);
				/*
				for (int q = 0; q < gamepad.userCards.length; q++){
				gamepad.userCards[q].removeCardFromPanel();
				}
				for (int q = 0; q < gamepad.enemyCards.length; q++){
				gamepad.enemyCards[q].removeCardFromPanel();
				}
				for (int q = 0; q < gamepad.handCards.length; q++){
				gamepad.handCards[q].removeCardFromPanel();
				}
				*/
				controlpad.endTurnButton.setEnabled(false);
				gamepad.userPlayer.clearHand();
				gamepad.enemyPlayer.clearBoard();
				gamepad.enemyPlayer.clearHand();
				gamepad.userPlayer.clearBoard();
				/*
				gamepad.enemyWcp.setText("");
				gamepad.userWcp.setText("");
				gamepad.deckLabel.setText("");
				gamepad.deckLabel = new JLabel();
				gamepad.deckPanel.removeAll();
				gamepad.enemyHandLen.setText("");
				gamepad.enemyRemainingCards.setText("");
				gamepad.enemyHealth.setText("");
				gamepad.userRemainingCards.setText("");
				gamepad.userHealth.setText("");
				gamepad.userHand.removeAll();
				gamepad.userTable.removeAll();
				gamepad.enemyTable.removeAll();
				*/
				gamepad.updateBoardCards();
				gamepad.updateHandCards();
				gamepad.hideBoard();
				gamepad.revalidate();
				gamepad.repaint();
				gamepad.setEndOfTurn(true);
				controlpad.statusText.setText("Please create or join a game");
				if (gamepad.endGame == false)
				{
					chatpad.chatLineArea.append("Game>You Lost! You have surrended the game!\n");
					chatpad.chatLineArea.setCaretPosition(
						chatpad.chatLineArea.getText().length());			//gameclient.gamepad.isMouseEnabled=false;				
				}
				else
				{
						chatpad.chatLineArea.append("Game>Quitting old game\n");
						chatpad.chatLineArea.setCaretPosition(
								chatpad.chatLineArea.getText().length());			//gameclient.gamepad.isMouseEnabled=false;				
				}
			}
			if(!isOnGame)
			{
				/*
				for (int q = 0; q < gamepad.userCards.length; q++){
				gamepad.userCards[q].removeCardFromPanel();
				}
				for (int q = 0; q < gamepad.enemyCards.length; q++){
				gamepad.enemyCards[q].removeCardFromPanel();
				}
				for (int q = 0; q < gamepad.handCards.length; q++){
				gamepad.handCards[q].removeCardFromPanel();
				}
				*/
				controlpad.creatGameButton.setEnabled(true);
				controlpad.joinGameButton.setEnabled(true);
				controlpad.cancelGameButton.setEnabled(false);
				controlpad.endTurnButton.setEnabled(false);
				gamepad.userPlayer.clearHand();
				gamepad.userPlayer.clearBoard();
				gamepad.enemyPlayer.clearBoard();
				gamepad.enemyPlayer.clearHand();
				/*
				gamepad.enemyWcp.setText("");
				gamepad.userWcp.setText("");
				gamepad.deckLabel.setText("");
				gamepad.deckLabel = new JLabel();
				gamepad.deckPanel.removeAll();
				gamepad.userHand.removeAll();
				gamepad.userTable.removeAll();
				gamepad.enemyTable.removeAll();
				gamepad.enemyHandLen.setText("");
				gamepad.enemyRemainingCards.setText("");
				gamepad.enemyHealth.setText("");
				gamepad.userRemainingCards.setText("");
				gamepad.userHealth.setText("");
				*/
				gamepad.hideBoard();
				gamepad.updateBoardCards();
				gamepad.updateHandCards();
				gamepad.revalidate();
				gamepad.repaint();
				gamepad.gamethread.sendMessage("/giveup "+gameClientName);
				chatpad.chatLineArea.append("Quitting old game\n");
				chatpad.chatLineArea.setCaretPosition(
						chatpad.chatLineArea.getText().length());			//gameclient.gamepad.isMouseEnabled=false;
				controlpad.statusText.setText("Please create or join a game");
				gamepad.setEndOfTurn(true);
			}
			isClient=isServer=false;
		}
		else if (e.getSource() == controlpad.readyButton)
		{
			gamepad.gamethread.sendMessage("/" + gamepad.peerName+ " /chess " + gamepad.userPlayer.toString());
			controlpad.readyButton.setEnabled(false);
		}
		else if (e.getSource() == controlpad.endTurnButton)
		{
			controlpad.endTurnButton.setEnabled(false);
			gamepad.gamethread.sendMessage("/" + gamepad.peerName+ " /endTurn " + gamepad.yourName);
			chatpad.chatLineArea.append("Game>it is now the opponent's turn\n");
			chatpad.chatLineArea.setCaretPosition(
					chatpad.chatLineArea.getText().length());
			gamepad.setEndOfTurn(true);
			gamepad.userPlayer.resetHasAttacked();
		}
		
	}
	
	
	/**
	 * Sends message upon enter key pressed.
	 * @param e the KeyEvent
	 */
	public void keyPressed(KeyEvent e)
	{
		TextField inputWords=(TextField)e.getSource();
		
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if(inputpad.userChoice.getSelectedItem().equals("All"))
			{
				try
				{
					out.writeUTF(inputWords.getText());
					inputWords.setText("");
				}
				catch(Exception ea)
				{
					chatpad.chatLineArea.setText("chessClient:KeyPressed Connect failed, please retry \n");
					userpad.userList.removeAll();
					inputpad.userChoice.removeAll();
					inputWords.setText("");
					controlpad.connectButton.setEnabled(true);
				}
			}
			else
			{
				try
				{
					out.writeUTF("/"+inputpad.userChoice.getSelectedItem()+" "+inputWords.getText());
					inputWords.setText("");
				}
				catch(Exception ea)
				{
					chatpad.chatLineArea.setText("chessClient:KeyPressed Connect failed, please retry  \n");
					userpad.userList.removeAll();
					inputpad.userChoice.removeAll();
					inputWords.setText("");
					controlpad.connectButton.setEnabled(true);
				}
			}
		}
		
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
	}
	@Override
	public void keyReleased(KeyEvent e)
	{
	}
	
	
	
	public static void main(String args[])
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
		ProjectLima chessClient=new ProjectLima();
	}
}