package project.pad;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import project.card.*;
import project.card.Card.ability;
import project.deck.DeckofCards;

import project.lima.CardPanel;
import project.lima.Player;
import project.support.MuchCardsException;
import project.support.MuchCostException;
import project.thread.*;

/**
 * Class creates the GUI for the game board and handles game mechanics.
 * @author Eric
 */
public class GamePad extends Panel implements MouseListener
{
	public String actualName;							//name displayed to other users					
	public String yourName;								//name used in player class
	public Card[] gameCards = new Card[50];				//first deck of cards, for user player
	public Card[] gameCards2 = new Card[50];			//second deck for enemy player
	public Player userPlayer;							//represents player who is using this client
	public Player enemyPlayer;							//represents the opposing player
	
	public boolean secondClick;							//when true, specifies that a card with an on target ability has been played
	public Card savedCard;								//played card, with on target ability
	
	public static Color white = new Color(255,255,255);	//generic color
	
	//gui components
	//panels containing the cards
	public JPanel enemyHand = new JPanel();				//panel where enemy's stats are held
	public JPanel enemyTable = new JPanel();			//panel where enemy board cards are held
	public JPanel userHand = new JPanel();				//panel where user's had cards are held
	public JPanel userTable = new JPanel();				//panel where user's board cards are held
	//holds deck and user info
	public JPanel deckPanel = new JPanel();				//panel holding user player's stats
	public JLabel deckLabel = new JLabel();				//holds deck img
	public ImageIcon deck;
	public JLabel userHealth = new JLabel();			//user health lbl
	public JLabel userRemainingCards = new JLabel();	//remainign cards in user deck
	public JLabel userWcp = new JLabel();				//wildcat points (cost)
	public JLabel enemyHealth = new JLabel();			//enemy health
	public JLabel enemyRemainingCards = new JLabel();	//reamining cards in deck for enemy
	public JLabel enemyWcp = new JLabel();				//wildcat points (cost)
	public JLabel enemyHandLen = new JLabel();			//enemy hand length
		
	//panels of cards in player hand, player board, enemy board, placed in the corresponding parent panel
	public CardPanel[] handCards = new CardPanel[10];	
	public CardPanel[] userCards = new CardPanel[7];
	public CardPanel[] enemyCards = new CardPanel[7];
	
	//popup menus
	public JPopupMenu handMenu = new JPopupMenu();		//menu for hand
	public JPopupMenu userTableMenu = new JPopupMenu();	//menu for user board
	public JPopupMenu enemyTableMenu = new JPopupMenu();//menu for enemy board
	
	
	public JMenuItem getHandDescription = new JMenuItem("Get Description");
	public JMenuItem playCard = new JMenuItem("Play Card");
	public JMenuItem getUTableDescription = new JMenuItem("Get Description");
	public JMenuItem getETableDescription = new JMenuItem("Get Description");
	public JMenuItem attkPlayer = new JMenuItem("Attack Player");
	public JMenu attkCard = new JMenu("Attack Card");
	public JMenuItem useAbilityOnYou = new JMenuItem("Use Ability On");
	public JMenuItem useAbilityOnEnemy = new JMenuItem("Use Ability On");
	//add 7 items to submenu representing 7 possible cards that can be attacked
	public JMenuItem card0 = new JMenuItem("0");
	public JMenuItem card1 = new JMenuItem("1");
	public JMenuItem card2 = new JMenuItem("2");
	public JMenuItem card3 = new JMenuItem("3");
	public JMenuItem card4 = new JMenuItem("4");
	public JMenuItem card5 = new JMenuItem("5");
	public JMenuItem card6 = new JMenuItem("6");
	
	
	public int cIden;											//identifies the panel clicked
	
	public Socket gameSocket;									//game socket
	public DataInputStream inData;								//streams used in gamethread
	public DataOutputStream outData;
	
	
	public String selfName=null;							
	public String peerName=null;								//name of enemy player
	public String host=null;
	public int port=4331;
	public GameThread gamethread;
	
	public ChatPad chatpad;
	public boolean endGame;
	public boolean endTurn = true;
	
	public boolean martinPlayed;
	public int martinCnt;
	
	/**
	 * constructs the board GUI
	 * @param controlpad the game mechanic interface
	 * @param chatpad the chat and user-list interface
	 */
	public GamePad(ControlPad controlpad, ChatPad chatpad)
	{
		actualName = JOptionPane.showInputDialog(userTable, "Name:", "Enter a name:", 1);	
		yourName = actualName + "::";
		Random r = new Random();
		yourName += Math.abs(r.nextInt(100));
		yourName += Math.abs(r.nextInt(100));
		yourName += Math.abs(r.nextInt(100));												//actual name used by player class
		userPlayer = new Player(yourName, new DeckofCards(30, gameCards));					//create dummy values
		enemyPlayer = new Player("Eric", new DeckofCards(30, gameCards2));			
		
		this.chatpad = chatpad;
		gamethread = new GameThread(this, controlpad, chatpad);

		//set gamepad to use gridbag layout
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.ipady = 20;
		c.insets = new Insets(0,0,0,0);
		
		c.gridx = 0;
		c.gridy = 0;
		add(enemyHand, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		add(enemyTable, c);
		
		c.gridx = 0;
		c.gridy = 2;
		add(userTable, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.weighty = 0;
		add(userHand, c);
		
		
		//set layout of panels that make up the board
		FlowLayout cardsLayout = new FlowLayout();
		cardsLayout.setAlignment(FlowLayout.LEADING);
		FlowLayout cardsLayout2 = new FlowLayout();
		cardsLayout2.setAlignment(FlowLayout.CENTER);
		userHand.setLayout(cardsLayout);
		userHand.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userHand.setAlignmentY(Component.TOP_ALIGNMENT);
		userHand.setAlignmentX(Component.TOP_ALIGNMENT);
		
		userTable.setLayout(cardsLayout2);
		userTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		enemyTable.setLayout(cardsLayout2);
		enemyTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		enemyHand.setLayout(cardsLayout2);
		enemyHand.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		//set color to purple
		userHand.setBackground(new Color(106,49,163));
		enemyTable.setBackground(new Color(106,49,163));
		userTable.setBackground(new Color(106,49,163));
		enemyHand.setBackground(new Color(106,49,163));
		
		//opponent stats panel
		enemyHand.add(enemyHealth);
		enemyHand.add(enemyWcp);
		enemyHand.add(enemyRemainingCards);
		enemyHand.add(enemyHandLen);
		
		//initialize cardpanels
		for (int i = 0 ; i < 10; i++)
		{
			handCards[i] = new CardPanel();
		}
		for (int i = 0 ; i < 7; i++)
		{
			userCards[i] = new CardPanel();
		}
		for (int i = 0 ; i < 7; i++)
		{
			enemyCards[i] = new CardPanel();
		}
		
		//set panel that holds user stats
		deckPanel.setBackground(new Color(106,49,163));
		deckPanel.add(deckLabel);
		deckPanel.add(userHealth);
		deckPanel.add(userWcp);
		deckPanel.add(userRemainingCards);
		
		userHand.add(deckPanel);
		
		//set enemyHand layout
		enemyHand.setLayout(new BoxLayout(enemyHand, BoxLayout.Y_AXIS));
		enemyHand.setAlignmentY(Component.LEFT_ALIGNMENT);
		enemyHealth.setText("Enemy Health: " + String.valueOf(enemyPlayer.getCredits()));
		enemyHealth.setForeground(white);
		enemyWcp.setText("Wildcat Points: " + String.valueOf(enemyPlayer.getWcp()));
		enemyWcp.setForeground(white);
		enemyRemainingCards.setText("Cards Left: " + enemyPlayer.getDeck().cardsLeft());
		enemyRemainingCards.setForeground(white);
		
		//configure deck panel
		deckPanel.setLayout(new BoxLayout(deckPanel, BoxLayout.Y_AXIS));
		deck = resizeImage("img/Card_Back.jpg", 75, 90);
		deckLabel.setIcon(deck);
		userHealth.setText("Health: " + String.valueOf(userPlayer.getCredits()));
		userWcp.setText("Wildcat Points: " + String.valueOf(userPlayer.getWcp()));
		userRemainingCards.setText("Cards Left: " + userPlayer.getDeck().cardsLeft());
		
		
		updatePlayerStats();
		
		//add cardpanels to their corresponding parent panels
		for (int i = 0; i < 10; i ++)
		{
			userHand.add(handCards[i]);
		}
		for (int i = 0 ; i < 7; i ++ )
		{
			userTable.add(userCards[i]);
			enemyTable.add(enemyCards[i]);
		}
		
		
		updateHandCards();
		updateBoardCards();
		
		userHand.revalidate();
		userHand.repaint();
		
		
		//popup menu for cards in hand
		handMenu.add(getHandDescription);
		handMenu.add(playCard);
		
		//popup menu for user's board cards
		userTableMenu.add(getUTableDescription);
		userTableMenu.add(attkPlayer);
		userTableMenu.add(attkCard);
		userTableMenu.add(useAbilityOnYou);
		useAbilityOnYou.setEnabled(false);
		useAbilityOnEnemy.setEnabled(false);
		enemyTableMenu.add(getETableDescription);
		enemyTableMenu.add(useAbilityOnEnemy);
		
		//add 7 items to submenu representing 7 possible enemy cards
		attkCard.add(card0);
		attkCard.add(card1);
		attkCard.add(card2);
		attkCard.add(card3);
		attkCard.add(card4);
		attkCard.add(card5);
		attkCard.add(card6);
		
		//add mouse listeners for all menu components
		for (int i = 0; i < 10; i ++)
			handCards[i].addMouseListener(this);
		for (int i = 0; i < 7; i ++)
			userCards[i].addMouseListener(this);
		for (int i = 0; i < 7; i ++)
			enemyCards[i].addMouseListener(this);
		
		getHandDescription.addMouseListener(this);
		getUTableDescription.addMouseListener(this);
		getETableDescription.addMouseListener(this);
		playCard.addMouseListener(this);
		
		attkPlayer.addMouseListener(this);
		useAbilityOnEnemy.addMouseListener(this);
		useAbilityOnYou.addMouseListener(this);
		
		card0.addMouseListener(this);
		card1.addMouseListener(this);
		card2.addMouseListener(this);
		card3.addMouseListener(this);
		card4.addMouseListener(this);
		card5.addMouseListener(this);
		card6.addMouseListener(this);
		
	
		this.revalidate();
		this.repaint();
		hideBoard();			//hide the board
	}
	
	/**
	 * Displays the board when new game is created or joined.
	 * Makes all card and player statistics components visible.
	 */
	public void showBoard()
	{
		deckPanel.setVisible(true);
		userHealth.setVisible(true);
		userWcp.setVisible(true);
		userRemainingCards.setVisible(true);
		enemyHealth.setVisible(true);
		enemyWcp.setVisible(true);
		enemyRemainingCards.setVisible(true);
		enemyHandLen.setVisible(true);
		for (int i = 0 ; i < 10; i++)
		{
			handCards[i].setVisible(true);
		}
		for (int i = 0 ; i < 7; i ++)
		{
			userCards[i].setVisible(true);
			enemyCards[i].setVisible(true);
		}
		updatePlayerStats();
		updateBoardCards();
		updateHandCards();
	}
	
	/**
	 * Hides the board from users.
	 * Hides cards and player statistics.
	 */
	public void hideBoard()
	{
		deckPanel.setVisible(false);
		userHealth.setVisible(false);
		userWcp.setVisible(false);
		userRemainingCards.setVisible(false);
		enemyHealth.setVisible(false);
		enemyWcp.setVisible(false);
		enemyRemainingCards.setVisible(false);
		enemyHandLen.setVisible(false);
		for (int i = 0 ; i < 10; i++)
		{
			handCards[i].setVisible(false);
		}
		for (int i = 0 ; i < 7; i ++)
		{
			userCards[i].setVisible(false);
			enemyCards[i].setVisible(false);
		}
		
	}
	
	/**
         * connects to the server
         * @param ServerIP  IP address of server
         * @param ServerPort  port number of server
         * @return true if successful connect
         * @throws Exception if connecting failed, throw IO exception
         */
	public boolean connectServer(String ServerIP,int ServerPort) throws Exception
	{
		try
		{
			gameSocket=new Socket(ServerIP,ServerPort);
			inData=new DataInputStream(gameSocket.getInputStream());
			outData=new DataOutputStream(gameSocket.getOutputStream());
			gamethread.start();
			return true;
		}
		catch(IOException ex)
		{
			//statusText.setText("GamePad:connectServer:Can not connect \n");
		}
		return false;
	}
	
	/**
	 * Performs action upon mouse pressed.
	 * @param e The mouse event
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e) || e.isControlDown())
		{
			findMouseAction(e);
		}
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1)
		{
			findMouseAction(e);
		}
		Object a = e.getSource();
		JMenuItem temp = null;
		try {
			temp = (JMenuItem) a;
		}
		catch (Exception ex)
		{
			
		}
		if (temp != null && temp.isEnabled())
		{
			if (e.getSource() == getHandDescription)		//show card description
			{
				getCardDescription(handCards[cIden]);
			}
			if (e.getSource() == getUTableDescription)
			{
				getCardDescription(userCards[cIden]);
			}
			if (e.getSource() == getETableDescription)
			{
				getCardDescription(enemyCards[cIden]);
			}
			if (e.getSource() == playCard)
			{
				playCard(handCards[cIden]);
			}
			if (e.getSource() == attkPlayer)
			{
				attkPlayer(userCards[cIden]);
			}
			if (e.getSource() == card0)
			{
				attkCard(userCards[cIden], enemyCards[0]);
			}
			if (e.getSource() == card1)
			{
				attkCard(userCards[cIden], enemyCards[1]);
			}
			if (e.getSource() == card2)
			{
				attkCard(userCards[cIden], enemyCards[2]);
				
			}
			if (e.getSource() == card3)
			{
				attkCard(userCards[cIden], enemyCards[3]);
				
			}
			if (e.getSource() == card4)
			{
				attkCard(userCards[cIden], enemyCards[4]);
			}
			if (e.getSource() == card5)
			{
				attkCard(userCards[cIden], enemyCards[5]);
				
			}
			if (e.getSource() == card6)
			{
				attkCard(userCards[cIden], enemyCards[6]);
				
			}
			if (e.getSource() == useAbilityOnEnemy)
			{
				executeOnTarget(savedCard, enemyCards[cIden].getRegCard());
			}
			if (e.getSource() == useAbilityOnYou)
			{
				executeOnTarget(savedCard, userCards[cIden].getRegCard());
			}
			if (SwingUtilities.isLeftMouseButton(e) || e.isControlDown()){
				if (e.getComponent() == handCards[0])
				{
					cIden = 0;
					showPopup(e, "hand");
				}
				if (e.getComponent() == handCards[1])
				{
					cIden = 1;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[2])
				{
					cIden = 2;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[3])
				{
					cIden = 3;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[4])
				{
					cIden = 4;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[5])
				{
					cIden = 5;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[6])
				{
					cIden = 6;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[7])
				{
					cIden = 7;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[8])
				{
					cIden = 8;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[9])
				{
					cIden = 9;
					showPopup(e, "hand");
				}
				if (e.getComponent() == userCards[0])
				{
					cIden = 0;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[1])
				{
					cIden = 1;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[2])
				{
					cIden = 2;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[3])
				{
					cIden = 3;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[4])
				{
					cIden = 4;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[5])
				{
					cIden = 5;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[6])
				{
					cIden = 6;
					showPopup(e, "user");
				}
				if (e.getComponent() == enemyCards[0])
				{
					cIden = 0;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[1])
				{
					cIden = 1;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[2])
				{
					cIden = 2;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[3])
				{
					cIden = 3;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[4])
				{
					cIden = 4;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[5])
				{
					cIden = 5;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[6])
				{
					cIden = 6;
					showPopup(e, "enemy");
				}						}
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1)
			{
				findMouseAction(e);
			}
		}
	}
	
	/**
	 * Performs action upon mouse released
	 * @param e The mouse event
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		findMouseAction(e);
		
	}
	
	/**
	 * Perform action upon mouse entered.
	 * @param e The mouse event
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	/**
	 * Perform action upon mouse excited.
	 * @param e The mouse event
	 */
	@Override
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Perform action upon mouse clicked.
	 * @param e The mouse event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) || e.isControlDown())
		{
			findMouseAction(e);
		}
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1)
		{
			findMouseAction(e);
		}
		Object a = e.getSource();
		JMenuItem temp = null;
		try {
			temp = (JMenuItem) a;
		}
		catch (Exception ex)
		{
			
		}
		if (temp != null && temp.isEnabled())
		{
			if (e.getSource() == getHandDescription)		//show card description
			{
				getCardDescription(handCards[cIden]);
			}
			if (e.getSource() == getUTableDescription)
			{
				getCardDescription(userCards[cIden]);
			}
			if (e.getSource() == getETableDescription)
			{
				getCardDescription(enemyCards[cIden]);
			}
			if (e.getSource() == playCard)
			{
				playCard(handCards[cIden]);
			}
			if (e.getSource() == attkPlayer)
			{
				attkPlayer(userCards[cIden]);
			}
			if (e.getSource() == card0)
			{
				attkCard(userCards[cIden], enemyCards[0]);
			}
			if (e.getSource() == card1)
			{
				attkCard(userCards[cIden], enemyCards[1]);
			}
			if (e.getSource() == card2)
			{
				attkCard(userCards[cIden], enemyCards[2]);
				
			}
			if (e.getSource() == card3)
			{
				attkCard(userCards[cIden], enemyCards[3]);
				
			}
			if (e.getSource() == card4)
			{
				attkCard(userCards[cIden], enemyCards[4]);
			}
			if (e.getSource() == card5)
			{
				attkCard(userCards[cIden], enemyCards[5]);
				
			}
			if (e.getSource() == card6)
			{
				attkCard(userCards[cIden], enemyCards[6]);
				
			}
			if (e.getSource() == useAbilityOnEnemy)
			{
				executeOnTarget(savedCard, enemyCards[cIden].getRegCard());
			}
			if (e.getSource() == useAbilityOnYou)
			{
				executeOnTarget(savedCard, userCards[cIden].getRegCard());
			}
			if (SwingUtilities.isLeftMouseButton(e) || e.isControlDown()){
				if (e.getComponent() == handCards[0])
				{
					cIden = 0;
					showPopup(e, "hand");
				}
				if (e.getComponent() == handCards[1])
				{
					cIden = 1;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[2])
				{
					cIden = 2;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[3])
				{
					cIden = 3;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[4])
				{
					cIden = 4;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[5])
				{
					cIden = 5;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[6])
				{
					cIden = 6;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[7])
				{
					cIden = 7;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[8])
				{
					cIden = 8;
					showPopup(e, "hand");
					
				}
				if (e.getComponent() == handCards[9])
				{
					cIden = 9;
					showPopup(e, "hand");
				}
				if (e.getComponent() == userCards[0])
				{
					cIden = 0;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[1])
				{
					cIden = 1;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[2])
				{
					cIden = 2;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[3])
				{
					cIden = 3;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[4])
				{
					cIden = 4;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[5])
				{
					cIden = 5;
					showPopup(e, "user");
				}
				if (e.getComponent() == userCards[6])
				{
					cIden = 6;
					showPopup(e, "user");
				}
				if (e.getComponent() == enemyCards[0])
				{
					cIden = 0;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[1])
				{
					cIden = 1;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[2])
				{
					cIden = 2;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[3])
				{
					cIden = 3;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[4])
				{
					cIden = 4;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[5])
				{
					cIden = 5;
					showPopup(e, "enemy");
				}
				if (e.getComponent() == enemyCards[6])
				{
					cIden = 6;
					showPopup(e, "enemy");
				}						}
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1)
			{
				findMouseAction(e);
			}
		}
	}
	
	/**
	 * Retrieves information about cards from text file, creates actual cards used in the game.
	 */
	public void initCards()
	{
		martinPlayed = false;
		martinCnt = 0;
		endGame = false;
		//get regular card and ability card data
		try {
			Scanner scanner = new Scanner(new File("doc/Master RegCard List.txt"));
			int i = 0;
			while(scanner.hasNextLine())
			{
				String next = scanner.nextLine();
				String[] data = next.split(";");
				String name = data[0];
				int cost = Integer.parseInt(data[1]);
				String image = data[2];
				int attack = Integer.parseInt(data[3]);
				int health = Integer.parseInt(data[4]);
				String description = data[5];
				ability a = ability.valueOf(data[6]);
				gameCards[i] = new RegCard(name, cost, image, attack, health, description, a);
				gameCards2[i] = new RegCard(name, cost, image, attack, health, description, a);
				i++;
			}
			
			scanner = new Scanner(new File("doc/Master AbilityCard List"));
			while(scanner.hasNextLine())				//get ability cards
			{
				String next = scanner.nextLine();
				String[] data = next.split(",");
				String name = data[0];
				int cost = Integer.parseInt(data[1]);
				String image = data[2];
				String description = data[3];
				ability a = ability.valueOf(data[4]);
				gameCards[i] = new AbilityCard(name, cost, image, description, a);
				gameCards2[i] = new AbilityCard(name, cost, image, description, a);
				
				i++;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("file not found");
		}
		
		
		userPlayer = new Player(yourName, new DeckofCards(30, gameCards));			//initialize your hand
		enemyPlayer = new Player("Eric", new DeckofCards(30, gameCards2));			//initialize dummy enemy player hand
		userPlayer.initHand();
	}
	
	/**
	 * Depending on the mouse event, display the corresponding pop-up menu
	 * @param e The mouse event that occurred
	 * @param menu Tells the function which menu to display
	 */
	public void showPopup(MouseEvent e, String menu)
	{
		playCard.setEnabled(true);
		attkPlayer.setEnabled(true);
		attkCard.setEnabled(true);
		if (e.isPopupTrigger())
		{
			switch(menu)
			{
				case "hand":
					handMenu.show(e.getComponent(), e.getX(), e.getY());
					//disable if not player's turn
					if (endTurn == true || endGame == true)
					{
						playCard.setEnabled(false);
					}
					else
						playCard.setEnabled(true);
					break;
				case "user":
					userTableMenu.show(e.getComponent(), e.getX(), e.getY());
					if (endTurn == false && endGame == false)				//is the players turn
					{
						if (secondClick == false)		//check for on target ability
							useAbilityOnYou.setEnabled(false);
						if (userCards[cIden].getRegCard().hasAttacked() == true)		//check if card has attacked this turn
						{
							attkPlayer.setEnabled(false);
							attkCard.setEnabled(false);
						}
						else															//hasn't attacked this turn
						{
							attkPlayer.setEnabled(true);
							attkCard.setEnabled(true);
							if (enemyCards[0].isEmpty())
							{
								card0.setText("No Card");
								card0.setEnabled(false);
							}
							else
							{
								card0.setText(enemyCards[0].getCardName());
								card0.setEnabled(true);
							}
							if (enemyCards[1].isEmpty())
							{
								card1.setText("No Card");
								card1.setEnabled(false);
							}
							else
							{
								card1.setText(enemyCards[1].getCardName());
								card1.setEnabled(true);
							}
							if (enemyCards[2].isEmpty())
							{
								card2.setText("No Card");
								card2.setEnabled(false);
							}
							else
							{
								card2.setText(enemyCards[2].getCardName());
								card2.setEnabled(true);
							}
							if (enemyCards[3].isEmpty())
							{
								card3.setText("No Card");
								card3.setEnabled(false);
							}
							else
							{
								card3.setText(enemyCards[3].getCardName());
								card3.setEnabled(true);
							}
							if (enemyCards[4].isEmpty())
							{
								card4.setText("No Card");
								card4.setEnabled(false);
							}
							else
							{
								card4.setText(enemyCards[4].getCardName());
								card4.setEnabled(true);
							}
							if (enemyCards[5].isEmpty())
							{
								card5.setText("No Card");
								card5.setEnabled(false);
							}
							else
							{
								card5.setText(enemyCards[5].getCardName());
								card5.setEnabled(true);
							}
							if (enemyCards[6].isEmpty())
							{
								card6.setText("No Card");
								card6.setEnabled(false);
							}
							else
							{
								card6.setText(enemyCards[6].getCardName());
								card6.setEnabled(true);
							}
							card0.revalidate();
							card0.repaint();
							card1.revalidate();
							card1.repaint();
							card2.revalidate();
							card2.repaint();
							card3.revalidate();
							card3.repaint();
							card4.revalidate();
							card4.repaint();
							card5.revalidate();
							card5.repaint();
							card6.revalidate();
							card6.repaint();
						}
					}
					else			//not the players turn
					{
						attkCard.setEnabled(false);
						attkPlayer.setEnabled(false);
						useAbilityOnYou.setEnabled(false);
					}
					break;
				case "enemy":
					enemyTableMenu.show(e.getComponent(), e.getX(), e.getY());
					if (endTurn == false && endGame == false)			//your turn
					{
						if (secondClick == false)
							useAbilityOnEnemy.setEnabled(false);
						else
							useAbilityOnEnemy.setEnabled(true);
					}
					else							//not your turn
					{
						useAbilityOnEnemy.setEnabled(false);
					}
					break;
			}
		}
	}
	
	/**
	 * Show card's ability description
	 * @param card The card whose ability's description is to be retrieved
	 */
	public void getCardDescription(CardPanel card)
	{
		JOptionPane.showMessageDialog(userHand, (!card.getDescription().equals("") ? card.getDescription() : "Card has no ability"), "Description for " + card.getCardName(), 1, null);
	}
	
	/**
	 * Plays a card from the hand by determining whether it is an AbilityCard or a RegCard
	 * Puts a card onto the board (and executes ability if applicable) if RegCard, otherwise allows for use of ability
	 * @param card The card to be played
	 */
	public void playCard(CardPanel card)
	{
		useAbilityOnEnemy.setEnabled(false);
		useAbilityOnYou.setEnabled(false);
		try {															//try to play the card
			Card temp = userPlayer.playCard(cIden);
			if (temp instanceof AbilityCard)							//execute event if ability card, alert opponent of what occured
			{
				executeEvent((AbilityCard) temp);
				gamethread.sendMessage("/" + peerName+ " /gameMsg " + actualName + " has played:" + temp.getName() + "(" + temp.getDesc() + ")");
				gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
				gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());
				updatePlayerStats();
				updateBoardCards();
				updateHandCards();
				this.revalidate();
				this.repaint();
			}
			else														//check if reg card has ability, add card to board
			{
				String desc = !temp.getDesc().equals("") ? temp.getDesc() : "None";				
				if (temp.getAbility() != ability.NONE)
				{
					executeReg((RegCard) temp);
				}
				gamethread.sendMessage("/" + peerName+ " /gameMsg " + actualName + " has played:" + temp.getName() + "(" + desc + ")");
				userPlayer.addCardtoBoard(temp);
			}
			gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());				//update oponent board 
			gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());
			updateBoardCards();																		//update your board
			updateHandCards();
			updatePlayerStats();
			this.revalidate();
			this.repaint();			
		}
		catch (MuchCostException e)
		{
			JOptionPane.showMessageDialog(userHand, "Not enough Wildcat points!", "Error!", 1, null);
		}
		catch (MuchCardsException e)
		{
			JOptionPane.showMessageDialog(userHand, "No space left on board, only 7 cards allowed at a time.", "Invalid Action", 1, null);
		}
	}
	
	/**
	 * Finds image, given a file path and return resized image.
	 * @param filePath The file path of the image to be changed
	 * @param w Width
	 * @param h Height
	 * @return The new ImageIcon of desired width and height
	 */
	public static ImageIcon resizeImage(String filePath, int w, int h)
	{
		String data = filePath;
		BufferedImage bsrc, bdest;
		ImageIcon theIcon;
		
		try
		{
			
			bsrc = ImageIO.read(new File(data));
			
			bdest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bdest.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance((double) w / bsrc.getWidth(),
					(double) h / bsrc.getHeight());
			g.drawRenderedImage(bsrc, at);
			
			//add the scaled image
			theIcon = new ImageIcon(bdest);
			return theIcon;
		}
		catch (Exception e)
		{
			System.out.println("This image can not be resized. " + filePath + " Please check the path and type of file.");
			return null;
		}
		
		
		
		
	}
	
	/**
	 * Identifies which card that was just clicked in, and shows appropriate popup menu
	 * @param e The mouse event
	 */
	public void findMouseAction(MouseEvent e)
	{
		if (e.getComponent() == handCards[0])
		{
			cIden = 0;
			showPopup(e, "hand");
		}
		if (e.getComponent() == handCards[1])
		{
			cIden = 1;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[2])
		{
			cIden = 2;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[3])
		{
			cIden = 3;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[4])
		{
			cIden = 4;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[5])
		{
			cIden = 5;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[6])
		{
			cIden = 6;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[7])
		{
			cIden = 7;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[8])
		{
			cIden = 8;
			showPopup(e, "hand");
			
		}
		if (e.getComponent() == handCards[9])
		{
			cIden = 9;
			showPopup(e, "hand");
		}
		if (e.getComponent() == userCards[0])
		{
			cIden = 0;
			showPopup(e, "user");
		}
		if (e.getComponent() == userCards[1])
		{
			cIden = 1;
			showPopup(e, "user");
		}
		if (e.getComponent() == userCards[2])
		{
			cIden = 2;
			showPopup(e, "user");
		}
		if (e.getComponent() == userCards[3])
		{
			cIden = 3;
			showPopup(e, "user");
		}
		if (e.getComponent() == userCards[4])
		{
			cIden = 4;
			showPopup(e, "user");
		}
		if (e.getComponent() == userCards[5])
		{
			cIden = 5;
			showPopup(e, "user");
		}
		if (e.getComponent() == userCards[6])
		{
			cIden = 6;
			showPopup(e, "user");
		}
		if (e.getComponent() == enemyCards[0])
		{
			cIden = 0;
			showPopup(e, "enemy");
		}
		if (e.getComponent() == enemyCards[1])
		{
			cIden = 1;
			showPopup(e, "enemy");
		}
		if (e.getComponent() == enemyCards[2])
		{
			cIden = 2;
			showPopup(e, "enemy");
		}
		if (e.getComponent() == enemyCards[3])
		{
			cIden = 3;
			showPopup(e, "enemy");
		}
		if (e.getComponent() == enemyCards[4])
		{
			cIden = 4;
			showPopup(e, "enemy");
		}
		if (e.getComponent() == enemyCards[5])
		{
			cIden = 5;
			showPopup(e, "enemy");
		}
		if (e.getComponent() == enemyCards[6])
		{
			cIden = 6;
			showPopup(e, "enemy");
		}
	}
	
	/**
	 * Updates the cards in the player's hand
	 */
	public void updateHandCards()
	{
		int len = userPlayer.getHand().size();
		for (int i = 0 ; i < len; i ++ )
		{
			handCards[i].removeCardFromPanel();
			
			handCards[i].getNewCard(userPlayer.getHand().get(i));
			
			handCards[i].revalidate();
			handCards[i].repaint();
		}
		while (len < 10)
		{
			handCards[len].removeCardFromPanel();
			len++;
		}
		userHand.revalidate();
		userHand.repaint();
		
	}
	
	/**
	 * Updates the cards on the board
	 */
	public void updateBoardCards()
	{
		int uLen = userPlayer.getBoard().size();
		int eLen = enemyPlayer.getBoard().size();
		
		//remove card from board, if dead
		for (int i = uLen - 1; i >= 0 ; i--)
		{
			if (userPlayer.getBoard().get(i) instanceof RegCard)
			{
				RegCard reg = (RegCard) userPlayer.getBoard().get(i);
				if(reg.getHealth() <= 0)
					userPlayer.removeCardFromBoard(i);
			}
		}
		for (int i = eLen - 1; i >= 0 ; i --)
		{
			if (enemyPlayer.getBoard().get(i) instanceof RegCard)
			{
				RegCard reg = (RegCard) enemyPlayer.getBoard().get(i);
				if(reg.getHealth() <= 0)
					enemyPlayer.removeCardFromBoard(i);
			}
		}
		
		//rest CardPanels to represent what is actually in the player object's board
		uLen = userPlayer.getBoard().size();
		eLen = enemyPlayer.getBoard().size();
		for (int i = 0 ; i < uLen; i ++ )
		{
			userCards[i].removeCardFromPanel();
			userCards[i].getNewCard(userPlayer.getBoard().get(i));
			userCards[i].revalidate();
			userCards[i].repaint();
		}
		for (int i = 0 ; i < eLen; i ++)
		{
			enemyCards[i].removeCardFromPanel();
			enemyCards[i].getNewCard(enemyPlayer.getBoard().get(i));
			enemyCards[i].revalidate();
			enemyCards[i].repaint();
		}
		
		//remove extras
		while (uLen < 7)
		{
			userCards[uLen].removeCardFromPanel();
			uLen++;
		}
		while (eLen < 7)
		{
			enemyCards[eLen].removeCardFromPanel();
			eLen++;
		}
		enemyTable.revalidate();
		enemyTable.repaint();
		userTable.revalidate();
		userTable.repaint();
	}
	
	/**
	 * Update the various statistics of the player, such as health and WCP
	 */
	public void updatePlayerStats()
	{
		userHealth.setText("Health: " + String.valueOf(userPlayer.getCredits()));
		userWcp.setText("Wildcat Points: " + String.valueOf(userPlayer.getWcp()));
		userRemainingCards.setText("Cards Left: " + userPlayer.getDeck().cardsLeft());
		enemyHealth.setText("Enemy Health: " + String.valueOf(enemyPlayer.getCredits()));
		enemyWcp.setText("Wildcat Points: " + String.valueOf(enemyPlayer.getWcp()));
		enemyRemainingCards.setText("Cards Left: " + enemyPlayer.getDeck().cardsLeft());
		enemyHandLen.setText("Cards in hand: " + enemyPlayer.getHand().size());
		
		userHealth.setForeground(white);
		userWcp.setForeground(white);
		userRemainingCards.setForeground(white);
		enemyHealth.setForeground(white);
		enemyWcp.setForeground(white);
		enemyRemainingCards.setForeground(white);
		enemyHandLen.setForeground(white);
		
		if (userPlayer.getCredits() <= 0)
		{
			if (endGame == false)
				userLost();
		}
		else if (enemyPlayer.getCredits() <= 0)
		{
			if (endGame == false)
				userWon();
		}
	}
	
	public void userLost()
	{
		endGame = true;
		gamethread.sendMessage("/" + peerName+ " /youwin ");	
		
	}
	
	public void userWon()
	{
		endGame = true;
		gamethread.sendMessage("/" + peerName+ " /youlost ");				
	}
	/**
	 * Provides support for when a card attacks a player to subtract credits from them
	 * @param cardPanel determines which card attacks
	 */
	public void attkPlayer(CardPanel cardPanel)
	{
		RegCard card = cardPanel.getRegCard();
		int attack = card.getAttack();
		enemyPlayer.setCredits(-attack);
		card.setHasAttacked(true);
		gamethread.sendMessage("/" + peerName+ " /gameMsg " + actualName + " has attacked " + peerName.substring(9) + " with:" + card.getName() + "(" + card.getAttack()  + "/" + card.getHealth() + ")");
		gamethread.sendMessage("/" + peerName+ " /gameMsg " + peerName.substring(9) + " is now at " + enemyPlayer.getCredits() + " health");
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());				//send serialzed data of userPlayer and enemyPlayer
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());
		updatePlayerStats();
		updateBoardCards();
		updateHandCards();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Provides support for when a card attacks another card... implements card interaction
	 * @param attkPanel the attacking card
	 * @param defPanel the defending card
	 */
	public void attkCard(CardPanel attkPanel, CardPanel defPanel)
	{
		RegCard attacker = attkPanel.getRegCard();
		RegCard defender = defPanel.getRegCard();
		gamethread.sendMessage("/" + peerName+ " /gameMsg " + actualName + "has attacked " + defender.getName() + "(" + defender.getAttack() + "/" + defender.getHealth() + ")" + " with:" + attacker.getName() + "(" + attacker.getAttack()  + "/" + attacker.getHealth() + ")");
		attacker.setHealth(attacker.getHealth()-defender.getAttack());
		defender.setHealth(defender.getHealth()-attacker.getAttack());
		attacker.setHasAttacked(true);
		//determine which message to send
		if (attacker.getHealth() > 0)
			gamethread.sendMessage("/" + peerName+ " /gameMsg " + actualName + "'s" + attacker.getName() + " is now " + attacker.getAttack() + "/" + attacker.getHealth());
		else
			gamethread.sendMessage("/" + peerName+ " /gameMsg " + peerName.substring(9) + "'s" + attacker.getName() + " is now " + attacker.getAttack() + "/" + attacker.getHealth());
		
		if (defender.getHealth() > 0)
			gamethread.sendMessage("/" + peerName+ " /gameMsg " + peerName.substring(9) + "'s" + defender.getName() + " is now " + defender.getAttack() + "/" + defender.getHealth());
		else
			gamethread.sendMessage("/" + peerName+ " /gameMsg " + peerName.substring(9) + "'s" + defender.getName() + " is now dead");
		
		//send serialzed version of player objects
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());
		updateBoardCards();
		updateHandCards();
		updatePlayerStats();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * executes a RegCard, providing unique support for each RegCard's ability
	 * @param r the RegCard to be executed
	 */
	public void executeReg(RegCard r)
	{
		ability abil = r.getAbility();
		if ((abil == ability.STlowerattackby1) || (abil == ability.STdamage1) || (abil == ability.STdamage2)
				|| (abil == ability.STdamage3) || (abil == ability.STheal1) || (abil == ability.STheal2)
				|| (abil == ability.STheal3) || (abil == ability.STincattack1) || (abil == ability.STincattack3)
				|| (abil == ability.STreturnhand) || (abil == ability.STattackbuff1)) // Single Target abilities
		{
			switch(abil)
			{
				case STlowerattackby1:
				case STdamage1:
				case STdamage3:
				case STreturnhand:
				case STdamage2:
					useAbilityOnEnemy.setEnabled(true);
					break;
				case STincattack3:
				case STincattack1:
				case STheal2:
				case STheal3:
					useAbilityOnYou.setEnabled(true);
					break;
			}
			savedCard = r;
			secondClick = true;
		}
		else									//area of effect abilities
		{
			switch (abil)
			{
				case destroyallfriendlies:   // William Jennings Bryant
					int uLen = userPlayer.getBoard().size() - 1;
					for (int i = uLen; i >= 0 ; i--)
					{
						if (userCards[i].getRegCard() != null )
							if (userCards[i].getRegCard().getAbility() != ability.destroyallfriendlies)
							{
								userCards[i].removeCardFromPanel();
								userPlayer.removeCardFromBoard(i);
							}
					}
					break;
				case selfdamage3:    // Rob
					userPlayer.setCredits(-3);
					break;
				case selfheal2: // Deering Library
					userPlayer.setCredits(2);
					break;
				case AOEdamage1:  // Campus Skunk
					userPlayer.setCredits(-1);
					enemyPlayer.setCredits(-1);
					for (int i = 0; i < userCards.length; i++)
					{
						if (userCards[i].getRegCard() != null){
							if (userCards[i].getRegCard().getAbility() != ability.AOEdamage1)
								
							{
								userCards[i].getRegCard().setHealth(userCards[i].getRegCard().getHealth() - 1);
							}
						}
					}
					for (int j = 0; j < enemyCards.length; j++)
					{
						if (enemyCards[j].getRegCard() != null){
							enemyCards[j].getRegCard().setHealth(enemyCards[j].getRegCard().getHealth() - 1);
						}
					}
					break;
				case AOEfincdamage1:  // Willie the Wildcat
					for (int i = 0; i < userCards.length; i++)
					{
						if (userCards[i].getRegCard() != null){
							if (userCards[i].getRegCard().getAbility() != ability.AOEfincdamage1)
							{
								userCards[i].getRegCard().setAttack(userCards[i].getRegCard().getAttack() + 1);
							}
						}
					}
					
					break;
				case AOEfheal1: // Dolphin Show
					for (int i = 0; i < userCards.length; i++)
					{
						if (userCards[i].getRegCard() != null){
							userCards[i].getRegCard().setHealth(userCards[i].getRegCard().getHealth() + 1);
						}
					}
					break;
				case selfdamage7: // Charles Heston
					userPlayer.setCredits(-7);
					break;
				case AOEfinchealth3:  // Cindy Crawford
					for (int i = 0; i < userCards.length; i++)
					{
						if (userCards[i].getRegCard() != null){
							userCards[i].getRegCard().setHealth(userCards[i].getRegCard().getHealth() + 3);
						}
					}
					break;
				case AOEfhealthbuff2: // Dormitory Ant
					for (int i = 0; i < userCards.length; i++)
					{
						if (userCards[i].getRegCard() != null){
							userCards[i].getRegCard().setHealth(userCards[i].getRegCard().getHealth() + 2);
						}
					}
				case george:
					martinPlayed = true;
					break;
			}
		}
	}
	
	/**
	 * executes an AbilityCard, providing unique support for each AbilityCard's ability
	 * @param a the AbilityCard to be executed
	 */
	public void executeEvent(AbilityCard a)
	{
		ability abil = a.getAbility();
		
		if ((abil == ability.STlowerattackto1) || (abil == ability.STdamage6))  // for the cards that need to select a card to operate on
		{
			savedCard = a;
			secondClick = true;
			useAbilityOnEnemy.setEnabled(true);
			//remember to tell use to choose target if ability works on target
		}
		else
		{
			switch (abil)   // cards whose effect does not require a choice
			{
				case OPdamage5: // EEcs midterm
					enemyPlayer.setCredits(-5);
					break;
				case OPdamage14: // DilloDay
					enemyPlayer.setCredits(-14);
					break;
				case OPdamage8:  // Organic Chemistry
					enemyPlayer.setCredits(-8);
					break;
				case AOEedamage1:  // Winter Blizzard
					for (int i = 0; i < enemyCards.length; i++)
						if (enemyCards[i].getRegCard() != null)
						{
							enemyCards[i].getRegCard().setHealth(enemyCards[i].getRegCard().getHealth() - 1);
						}
					break;
				case AOEedamage3:
					for (int i = 0; i < enemyCards.length; i++)
						if (enemyCards[i].getRegCard() != null)
						{
							enemyCards[i].getRegCard().setHealth(enemyCards[i].getRegCard().getHealth() - 3);
						}
					break;
				case AOEfheal3:
					for (int i = 0; i < userCards.length; i++)
						if (userCards[i].getRegCard() != null)
						{
							int newHealth = userCards[i].getRegCard().getHealth() + 3;
							if (newHealth > userCards[i].getRegCard().getMaxHealth())
							{
								userCards[i].getRegCard().setHealth(userCards[i].getRegCard().getMaxHealth());
							}
							else
							{
								userCards[i].getRegCard().setHealth(userCards[i].getRegCard().getHealth() + 3);
							}
						}
					break;
				case firedrill:
					int uLen = userPlayer.getBoard().size() - 1;
					int eLen = enemyPlayer.getBoard().size() - 1;
					for (int i = uLen ; i >= 0; i--)
					{
						userPlayer.addCardtoHand(i);
						userCards[i].removeCardFromPanel();
						
					}
					for (int i = eLen ; i >= 0; i--)
					{
						enemyPlayer.addCardtoHand(i);
						enemyCards[i].removeCardFromPanel();
					}
					break;
			}
			
		}
		
		//serialze player objects
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());
		updatePlayerStats();
		updateHandCards();
		updateBoardCards();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * provides support for various RegCards and AbilityCards whose abilities require selecting a specific target Card
	 * @param s the acting card to be executed
	 * @param t the target card
	 */
	public void executeOnTarget(Card s, RegCard t){
		ability abil = s.getAbility();
		
		
		switch (abil)
		{
			case STlowerattackby1:  // University Squirrel and Kresge
				t.setAttack(t.getAttack() - 1);
				break;
			case STdamage1:  // Lake Seagull and Evanston Raccoon
				t.setHealth(t.getHealth() - 1);
				break;
			case STdamage2:  // House Centipede
				t.setHealth(t.getHealth() - 2);
				break;
			case STincattack3: // Lakefill Pond Carp
				t.setAttack(t.getAttack() + 3);
				break;
			case STincattack1: // Pat Fitzgerald
				t.setAttack(t.getAttack() + 1);
				break;
			case STdamage3: // Seth Meyers
				t.setHealth(t.getHealth() - 3);
				break;
			case STreturnhand:  // Elusive Rabbit and Deering Meadow
				int eLen = enemyCards.length - 1;
				for (int j = eLen; j >= 0; j--)
				{
					if (enemyCards[j].getRegCard() == t)
					{
						enemyPlayer.addCardtoHand(j);
						break;
					}
				}
				break;
			case STdamage6: //Chicago Light Pollution
				t.setHealth(t.getHealth() - 6);
				break;
			case STlowerattackto1:  // the rock
				t.setAttack(1);
				break;
			case STheal1:
				t.setHealth(t.getHealth() + 1);
				break;
			case STheal2:  // Allison Hall
				t.setHealth(t.getHealth() + 2);
				break;
			case STheal3:  // Bobb McCulloch Hall
				t.setHealth(t.getHealth() + 3);
				break;
			case STattackbuff1:
				t.setAttack(t.getAttack() + 1);
				break;
				
		}
		useAbilityOnEnemy.setEnabled(false);
		useAbilityOnYou.setEnabled(false);
		secondClick = false;
		gamethread.sendMessage("/" + peerName+ " /gameMsg " + actualName + " has used " + s.getName() + "(" + s.getDesc()  + ")" +  "on " + t.getName());
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());
		updateBoardCards();
		updateHandCards();
		updatePlayerStats();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * used for regulating turns
	 * @param b the boolean value to which endTurn will be set
	 */
	public void setEndOfTurn(boolean b)
	{
		endTurn = b;
		if (martinCnt > 2)
		{
			for (int i = 0 ; i < userPlayer.getBoard().size(); i++)
			{
				if (userPlayer.getBoard().get(i).getName().equals("George R.R. Martin"))
				{
					userPlayer.removeCardFromBoard(i);
					RegCard dragon = new RegCard("Dragon", 0, "img/Dragon.jpg", 10,10, "", ability.NONE);
					userPlayer.addCardtoBoard(dragon);
					updatePlayerStats();
					updateBoardCards();
					updateHandCards();
					gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());				//update oponent board
					gamethread.sendMessage("/" + peerName+ " /gameMsg " + actualName + "'s Martin has morphed into a dragon!");	
				}
			}
			martinPlayed = false;
			martinCnt = 0;
		}
		if (martinPlayed == true)
		{
			martinCnt++;
		}
	}
}
