/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;
/*
 * To do:
 * Get description for userboard cards, enemy board cards, - change to array, fix mouse listener to reflect this
 * Change to flow layout for cards - done
 * ??
 * ??
 * ??
 * ??
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

/**
 *
 * @author Eric
 */
public class BoardGui {
	
	public static Card[] gameCards = new Card[50];
	protected static Player userPlayer = new Player("Eric", new DeckofCards(30, gameCards));			//test not working
	protected static Player enemyPlayer = new Player("Eric", new DeckofCards(30, gameCards));
	
	
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
				case "userTable":
					userTableMenu.show(e.getComponent(), e.getX(), e.getY());
					break;
				case "enemyTable":
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
		
		
		
		for (int i = 0 ; i < 9; i++)
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
		
		
		
		enemyHand.setBackground(Color.black);
		enemyTable.setBackground(Color.red);
		userTable.setBackground(Color.yellow);
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
		
		//set layout of panels that make up the board
		FlowLayout cardsLayout = new FlowLayout();
		cardsLayout.setAlignment(FlowLayout.LEADING);
		userHand.setLayout(cardsLayout);
		userHand.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userTable.setLayout(cardsLayout);
		userTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		enemyTable.setLayout(cardsLayout);
		enemyTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		enemyHand.setLayout(cardsLayout);
		enemyHand.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//and other panels
		try {
			deckPanel.setLayout(new BoxLayout(deckPanel, BoxLayout.Y_AXIS));
			deck = resizeImage("src/project/214IMAGES/Card Back.jpg", 100, 100);
			deckLabel = new JLabel(deck);
			deckPanel.add(deckLabel);
			userHealth.setText("Health: " + String.valueOf(userPlayer.getCredits()));
			deckPanel.add(userHealth);
			userWcp.setText("Wildcat Points: " + String.valueOf(userPlayer.getWcp()));
			deckPanel.add(userWcp);
			userRemainingCards.setText("Cards Left: " + userPlayer.getDeck().cardsLeft());
			deckPanel.add(userRemainingCards);
			userHand.add(deckPanel);
			handCards[0] = new CardPanel(gameCards[0]);
			userHand.add(handCards[0]);
			handCards[1] = new CardPanel(gameCards[1]);
			userHand.add(handCards[1]);
			handCards[2] = new CardPanel(gameCards[2]);
			userHand.add(handCards[2]);
			handCards[3] = new CardPanel(gameCards[3]);
			userHand.add(handCards[3]);
			handCards[4] = new CardPanel(gameCards[4]);
			userHand.add(handCards[4]);
			handCards[5] = new CardPanel(gameCards[10]);
			userHand.add(handCards[5]);
			handCards[6] = new CardPanel(gameCards[6]);
			userHand.add(handCards[6]);
			handCards[7] = new CardPanel(gameCards[7]);
			userHand.add(handCards[7]);
			handCards[8] = new CardPanel(gameCards[8]);
			userHand.add(handCards[8]);
			handCards[9] = new CardPanel(gameCards[9]);
			userHand.add(handCards[9]);
			userHand.revalidate();
			userHand.repaint();
			
			handMenu.add(getHandDescription);
			handMenu.add(playCard);
			
			userTableMenu.add(getUTableDescription);
			userTableMenu.add(attkPlayer);
			userTableMenu.add(attkCard);
			
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
					if (e.getSource() == playCard)
					{
						playCard(handCards[cIden]);
						
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
				
				chatBtn.addActionListener(gameActListener);
				chatMsgWin.addKeyListener(enterKey);
				
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
				leftSide.add(new JScrollPane(userList), gl);
				
				gl.gridx = 0;
				gl.gridy = 2;
				leftSide.add(chatLbl, gl);
				
				gl.gridx = 0;
				gl.gridy = 3;
				gl.ipady = 180;
				leftSide.add(new JScrollPane(chatWindow), gl);
				
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
		System.out.println("Send button action");
	}
	
	//show card's ability description
	public static void getCardDescription(CardPanel card)
	{
		JOptionPane.showMessageDialog(userHand, (!card.getDescription().equals("") ? card.getDescription() : "Card has no ability"), "Description for " + card.getCardName(), 1, null);
	}
	
	//put card in hand onto board
	public static void playCard(CardPanel card)
	{
		
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
	
	
	
}
