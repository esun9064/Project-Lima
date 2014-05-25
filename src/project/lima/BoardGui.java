/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;
/*
 * To do:
 * add attack card code
 * finish enum code
 * create listener/actions for abilities
 * merge with p2p
 * borderfactory for panels
 * make pretty
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import project.card.AbilityCard;
import project.card.Card;
import project.card.Card.ability;
import project.card.RegCard;
import project.deck.DeckofCards;
import project.support.MuchCostException;

/**
 *
 * @author Eric
 */
public class BoardGui {
	
	public static Card[] gameCards = new Card[50];
	
	protected static Player userPlayer;			//test not working
	protected static Player enemyPlayer;
	
	public static boolean secondClick;
	public static Card savedCard; 
	
	public static Color white = new Color(255,255,255);
	
	//gui components
	public static JFrame frame = new JFrame("Lima");		//overall frame of the game
	public static JPanel gameArea = new JPanel();			//main panel (left side holds chat, center holds board)
	public static JPanel board = new JPanel();				//panel holds game board (aka cards on board and hand)
	//panels containing the cards
	public static JPanel enemyHand = new JPanel();
	public static JPanel enemyTable = new JPanel();
	public static JPanel userHand = new JPanel();
	public static JPanel userTable = new JPanel();
	//holds deck and user info
	public static JPanel deckPanel = new JPanel();
	public static JLabel deckLabel = new JLabel();
	public static ImageIcon deck;
	public static JLabel userHealth = new JLabel();
	public static JLabel userRemainingCards = new JLabel();
	public static JLabel userWcp = new JLabel();				//wildcat points (cost)
	public static JLabel enemyHealth = new JLabel();
	public static JLabel enemyRemainingCards = new JLabel();
	public static JLabel enemyWcp = new JLabel();				//wildcat points (cost)
	public static JLabel enemyHandLen = new JLabel();
	
	//panels of cards in player hand, change to array, later!!!!!!!!!!! -
	public static CardPanel[] handCards = new CardPanel[10];
	public static CardPanel[] userCards = new CardPanel[7];
	public static CardPanel[] enemyCards = new CardPanel[7];
	
	
	//popup menus
	public static JPopupMenu handMenu = new JPopupMenu();	//menu for hand
	public static JPopupMenu userTableMenu = new JPopupMenu();	//menu for hand
	public static JPopupMenu enemyTableMenu = new JPopupMenu();	//menu for hand
	
	
	public static JMenuItem getHandDescription = new JMenuItem("Get Description");
	public static JMenuItem playCard = new JMenuItem("Play Card");
	public static JMenuItem getUTableDescription = new JMenuItem("Get Description");
	public static JMenuItem getETableDescription = new JMenuItem("Get Description");
	public static JMenuItem attkPlayer = new JMenuItem("Attack Player");
	public static JMenu attkCard = new JMenu("Attack Card");
	public static JMenuItem useAbility = new JMenuItem("Use Ability");
	//add ten items to submenu representing 10 possible cards
	public static JMenuItem card0 = new JMenuItem("0");
	public static JMenuItem card1 = new JMenuItem("1");
	public static JMenuItem card2 = new JMenuItem("2");
	public static JMenuItem card3 = new JMenuItem("3");
	public static JMenuItem card4 = new JMenuItem("4");
	public static JMenuItem card5 = new JMenuItem("5");
	public static JMenuItem card6 = new JMenuItem("6");
	
	//overall menubar
	public static JMenuBar gameMenu = new JMenuBar();
	public static JMenu file = new JMenu("File");
	public static JMenuItem quit = new JMenuItem("Quit");
	
	//leftside holds communication components
	public static JPanel leftSide = new JPanel();
	public static JLabel userLbl = new JLabel("Currently Online:");
	public static JTextArea userList = new JTextArea(12,25);				//list of currently online users
	public static JLabel chatLbl = new JLabel("Game Chat:");
	public static JTextArea chatWindow = new JTextArea(12,25);			//displays incoming/outgoing messages
	public static JTextField chatMsgWin = new JTextField(19);				//component for typing messages
	public static JButton chatBtn = new JButton("Send");					//send message button
	
	public static int cIden;											//identifies the panel clicked
	
	/**
	 * Retrieves information about cards from text file, creates actual cards used in the game.
	 */
	public static void initCards()
	{
		//get regular card data
		try {
			Scanner scanner = new Scanner(new File("doc/Master RegCard List.txt"));
			int i = 0;
			while(scanner.hasNextLine())			//get regular cards
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
				i++;
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		userPlayer = new Player("Eric", new DeckofCards(30, gameCards));			//test not working
		enemyPlayer = new Player("Eric", new DeckofCards(30, gameCards));
		
		
		userPlayer.clearHand();
		for (int i = 41 ; i < 50 ; i ++)
			userPlayer.addCardToHand(gameCards[i]);
		userPlayer.addCardToHand(gameCards[20]);
	}
	public static void showPopup(MouseEvent e, String menu)
	{
		if (e.isPopupTrigger())
		{
			switch(menu)
			{
				case "hand":
					handMenu.show(e.getComponent(), e.getX(), e.getY());
					break;
				case "user":
					userTableMenu.show(e.getComponent(), e.getX(), e.getY());
					if (userCards[cIden].getCard().getAbility().compareTo(ability.NONE) == 0)
						useAbility.setEnabled(false);
					else
						useAbility.setEnabled(true);
					if (userCards[cIden].getRegCard().hasAttacked() == true)
					{
						attkPlayer.setEnabled(false);
						attkCard.setEnabled(false);
					}
					else
					{
						attkPlayer.setEnabled(true);
						attkCard.setEnabled(true);
					}
					break;
				case "enemy":
					enemyTableMenu.show(e.getComponent(), e.getX(), e.getY());
					break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		initCards();
		
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(1366, 720));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameArea.setLayout(new BorderLayout());
		frame.setContentPane(gameArea);
		//board holds the cards
		GridBagConstraints c = new GridBagConstraints();
		board.setLayout(new GridBagLayout());
		
		
		
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
		
		
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.ipady = 20;
		c.insets = new Insets(0,0,0,0);
		
		c.gridx = 0;
		c.gridy = 0;
		board.add(enemyHand, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		board.add(enemyTable, c);
		
		c.gridx = 0;
		c.gridy = 2;
		board.add(userTable, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.weighty = 0;
		board.add(userHand, c);
		
		//set enemyHand layout
		enemyHand.setLayout(new BoxLayout(enemyHand, BoxLayout.Y_AXIS));
		enemyHand.setAlignmentY(Component.LEFT_ALIGNMENT);
		enemyHealth.setText("Enemy Health: " + String.valueOf(enemyPlayer.getCredits()));
		enemyHealth.setForeground(white);
		enemyHand.add(enemyHealth);
		enemyWcp.setText("Wildcat Points: " + String.valueOf(enemyPlayer.getWcp()));
		enemyWcp.setForeground(white);
		enemyHand.add(enemyWcp);
		enemyRemainingCards.setText("Cards Left: " + enemyPlayer.getDeck().cardsLeft());
		enemyRemainingCards.setForeground(white);
		enemyHand.add(enemyRemainingCards);
		enemyHand.add(enemyHandLen);
		
		//set layout of panels that make up the board
		FlowLayout cardsLayout = new FlowLayout();
		cardsLayout.setAlignment(FlowLayout.LEADING);
		userHand.setLayout(cardsLayout);
		userHand.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userHand.setAlignmentY(Component.TOP_ALIGNMENT);
		userHand.setAlignmentX(Component.TOP_ALIGNMENT);
		
		userTable.setLayout(cardsLayout);
		userTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		enemyTable.setLayout(cardsLayout);
		enemyTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		enemyHand.setLayout(cardsLayout);
		enemyHand.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		userHand.setBackground(new Color(106,49,163));
		enemyTable.setBackground(new Color(106,49,163));
		userTable.setBackground(new Color(106,49,163));
		enemyHand.setBackground(new Color(106,49,163));
		
		//and other panels
		deckPanel.setLayout(new BoxLayout(deckPanel, BoxLayout.Y_AXIS));
		deckPanel.setBackground(new Color(106,49,163));
		deck = resizeImage("src/project/214IMAGES/Card Back.jpg", 75, 90);
		deckLabel = new JLabel(deck);
		deckPanel.add(deckLabel);
		userHealth.setText("Health: " + String.valueOf(userPlayer.getCredits()));
		deckPanel.add(userHealth);
		userWcp.setText("Wildcat Points: " + String.valueOf(userPlayer.getWcp()));
		deckPanel.add(userWcp);
		userRemainingCards.setText("Cards Left: " + userPlayer.getDeck().cardsLeft());
		deckPanel.add(userRemainingCards);
		userHand.add(deckPanel);
		updatePlayerStats();
		
		for (int i = 0; i < 10; i ++)
		{
			userHand.add(handCards[i]);
		}
		for (int i = 0 ; i < 7; i ++ )
		{
			userTable.add(userCards[i]);
			enemyTable.add(enemyCards[i]);
		}
		
		//test enemy card interaction
		for (int i = 0 ; i < 7 ; i ++)
		{
			try {
				Card temp = enemyPlayer.playCard(i);
				if (temp instanceof RegCard)
				{
					enemyPlayer.addCardtoBoard(temp);
					updateBoardCards();
					updatePlayerStats();
				}
				else
					i -= 1;
			}
			catch (Exception ex)
			{
				
			}
		}
		
		updateHandCards();
		updateBoardCards();
		
		userHand.revalidate();
		userHand.repaint();
		
		try {
			
			
			handMenu.add(getHandDescription);
			handMenu.add(playCard);
			
			userTableMenu.add(getUTableDescription);
			userTableMenu.add(attkPlayer);
			userTableMenu.add(attkCard);
			userTableMenu.add(useAbility);
			
			enemyTableMenu.add(getETableDescription);
			
			//add ten items to submenu representing 10 possible cards
			attkCard.add(card0);
			attkCard.add(card1);
			attkCard.add(card2);
			attkCard.add(card3);
			attkCard.add(card4);
			attkCard.add(card5);
			attkCard.add(card6);
			
			//listener for menu items
			ActionListener gameActListener = new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == getHandDescription)
					{
						//JOptionPane.showMessageDialog(frame, frame, , null);
					}
					if (e.getSource() == attkPlayer)
					{
						
					}
					if (e.getSource() == card0)
					{
						System.out.println("test card 0");
					}
					if (e.getSource() == card1)
					{
						
					}
					if (e.getSource() == card2)
					{
						
					}
					if (e.getSource() == card3)
					{
						
					}
					if (e.getSource() == card4)
					{
						
					}
					if (e.getSource() == card5)
					{
						
					}
					if (e.getSource() == card6)
					{
						
					}
					//menubar items
					if (e.getSource() == quit)
					{
						frame.dispose();
					}
					//send button
					if (e.getSource() == chatBtn)
					{
						sendButtonAction();
					}
					//end turn button
				}
				
			};
			
			//key listener for textField
			KeyListener enterKey = new KeyListener()
			{
				
				@Override
				public void keyTyped(KeyEvent e) {
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getSource() == chatMsgWin)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
							sendButtonAction();
					}
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getSource() == chatMsgWin)	//reset textField
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							chatMsgWin.setText("");
							chatMsgWin.requestFocus(true);
							chatMsgWin.setCaretPosition(0);
						}
				}
				
				
			};
			
			
			
			//listener for card actions - attack, get description etc
			MouseListener cardListener = new MouseListener()
			{
				@Override
				public void mouseClicked(MouseEvent e) {
					
					
				}
				
				
				@Override
				public void mousePressed(MouseEvent e) {
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
					if (SwingUtilities.isLeftMouseButton(e) || e.isControlDown()){
						findMouseAction(e);
					}
				}
				
				@Override
				public void mouseReleased(MouseEvent e) {
					findMouseAction(e);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
			};
			
			for (int i = 0; i < 10; i ++)
				handCards[i].addMouseListener(cardListener);
			for (int i = 0; i < 7; i ++)
				userCards[i].addMouseListener(cardListener);
			for (int i = 0; i < 7; i ++)
				enemyCards[i].addMouseListener(cardListener);
			
			getHandDescription.addMouseListener(cardListener);
			getUTableDescription.addMouseListener(cardListener);
			getETableDescription.addMouseListener(cardListener);
			playCard.addMouseListener(cardListener);
			
			attkPlayer.addMouseListener(cardListener);
			useAbility.addMouseListener(cardListener);
			
			card0.addMouseListener(cardListener);
			card1.addMouseListener(cardListener);
			card2.addMouseListener(cardListener);
			card3.addMouseListener(cardListener);
			card4.addMouseListener(cardListener);
			card5.addMouseListener(cardListener);
			card6.addMouseListener(cardListener);
			
			
			chatBtn.addActionListener(gameActListener);
			chatMsgWin.addKeyListener(enterKey);
			
			//menu uptop
			quit.addActionListener(gameActListener);
			
			//leftSide panel holds game chat window and current online user list
			leftSide.setLayout(new GridBagLayout());
			GridBagConstraints gl = new GridBagConstraints();
			gl.anchor = GridBagConstraints.LAST_LINE_START;
			gl.gridx = 0;
			gl.gridy = 0;
			gl.ipady = 6;
			leftSide.add(userLbl, gl);
			
			userList.setVisible(true);
			gl.anchor = GridBagConstraints.NORTHWEST;
			gl.gridx = 0;
			gl.weighty = 5;
			gl.gridwidth = 2;
			gl.gridy = 1;
			leftSide.add(new JScrollPane(userList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gl);
			
			gl.gridx = 0;
			gl.gridy = 2;
			leftSide.add(chatLbl, gl);
			
			gl.gridx = 0;
			gl.gridy = 3;
			gl.ipady = 180;
			leftSide.add(new JScrollPane(chatWindow, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gl);
			
			gl.gridy = 0;
			gl.gridy = 4;
			gl.gridwidth = 1;
			gl.weighty = 1;
			gl.ipady = 7;
			gl.weightx = .5;
			leftSide.add(chatMsgWin, gl);
			
			gl.gridx = 1;
			gl.gridy = 4;
			gl.ipady = 0;
			gl.weightx = .5;
			leftSide.add(chatBtn, gl);
			
			//add main menu items
			file.add(quit);
			gameMenu.add(file);
			
			
			
			
			
			
		}
		catch (Exception ex)
		{
			System.out.println("No file");
		}
		
		gameArea.add(gameMenu, BorderLayout.NORTH);
		gameArea.add(leftSide, BorderLayout.WEST);
		gameArea.add(board, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
	}
	
	public static void sendButtonAction()
	{
		chatWindow.append(chatMsgWin.getText().toString() + "\n");
	}
	
	//show card's ability description
	public static void getCardDescription(CardPanel card)
	{
		JOptionPane.showMessageDialog(frame, (!card.getDescription().equals("") ? card.getDescription() : "Card has no ability"), "Description for " + card.getCardName(), 1, null);
	}
	
	//put card in hand onto board
	public static void playCard(CardPanel card)
	{
		try {
			Card temp = userPlayer.playCard(cIden);
			if (temp instanceof AbilityCard)
			{
				executeEvent((AbilityCard) temp);
				updatePlayerStats();
				updateBoardCards();
				updateHandCards();
			}
			else
				userPlayer.addCardtoBoard(temp);
			updateBoardCards();
			updateHandCards();
			updatePlayerStats();
			chatWindow.append("GAME MESSAGE: " + userPlayer.getName() + " played: " + temp.getName() + "\n");	//would like to make game messages different color,
			//but this requires using JTextPane instead of editor, if someone would like to learn that...
		}
		catch (MuchCostException e)
		{
			JOptionPane.showMessageDialog(frame, "Not enough Wildcat points!", "Error!", 1, null);
		}
	}
	//helper method for resizing images
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
	
	
	
	public static void findMouseAction(MouseEvent e)
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
	
	public static void updateHandCards()
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
	
	public static void updateBoardCards()
	{	
		int uLen = userPlayer.getBoard().size();
		int eLen = enemyPlayer.getBoard().size();
		
		
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
	
	public static void updatePlayerStats()
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
	}
	
	public static void attkPlayer(CardPanel cardPanel)
	{
		RegCard card = cardPanel.getRegCard();
		int attack = card.getAttack();
		enemyPlayer.setCredits(-attack);
		updatePlayerStats();
		card.setHasAttacked(true);
	}
	
	public static void attkCard(CardPanel attkPanel, CardPanel defPanel)
	{
		RegCard attacker = attkPanel.getRegCard();
		RegCard defender = defPanel.getRegCard();
		attacker.setHealth(attacker.getHealth()-defender.getAttack());
		defender.setHealth(defender.getHealth()-attacker.getAttack());
		updateBoardCards();
	}
	
	public static void executeReg(RegCard r)
	{
		ability abil = r.getAbility();
		if ((abil == ability.STlowerattackby1) || (abil == ability.STdamage1) || (abil == ability.STdamage2)
				|| (abil == ability.STdamage3) || (abil == ability.STheal1) || (abil == ability.STheal2)
				|| (abil == ability.STheal3) || (abil == ability.STincattack1) || (abil == ability.STincattack3)
				|| (abil == ability.STreturnhand) || (abil == ability.STattackbuff1)) // Single Target abilities
		{
			savedCard = r;
			secondClick = true;
		}
		else
		{
			switch (abil)
			{
				case destroyallfriendlies:   // William Jennings Bryant
					for (int i = 0; i < userCards.length; i++)
					{
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
					
			}
			
		}
	}
	
	
	public static void executeEvent(AbilityCard a)
	{
		ability abil = a.getAbility();
		
		
		if ((abil == ability.STlowerattackto1) || (abil == ability.STdamage6))  // for the cards that need to select a card to operate on
		{
			savedCard = a;
			secondClick = true;
			
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
		
		updatePlayerStats();
		updateHandCards();
		updateBoardCards();
	}
	
	//needs additional input
	public static void executeOnTarget(Card s, RegCard t){
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
				for (int j = 0; j < enemyCards.length; j++)
				{
					if (enemyCards[j].getRegCard() == t)
						enemyPlayer.addCardtoHand(j);
					enemyPlayer.removeCardFromBoard(j);
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
		
		
	}
	
	
	
}
