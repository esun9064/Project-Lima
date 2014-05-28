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
import project.lima.Board;

import project.lima.CardPanel;
import project.lima.Player;
import project.support.MuchCardsException;
import project.support.MuchCostException;
import project.thread.*;

public class GamePad extends Panel implements MouseListener,ActionListener
{
	public static String yourName;
	public Card[] gameCards = new Card[50];
	public Card[] gameCards2 = new Card[50];
	public static Player userPlayer;			//test not working
	public static Player enemyPlayer;
	
	public boolean secondClick;
	public Card savedCard;
	
	public static Color white = new Color(255,255,255);
	
	//gui components
	//panels containing the cards
	public JPanel enemyHand = new JPanel();
	public JPanel enemyTable = new JPanel();
	public JPanel userHand = new JPanel();
	public JPanel userTable = new JPanel();
	//holds deck and user info
	public JPanel deckPanel = new JPanel();
	public JLabel deckLabel = new JLabel();
	public ImageIcon deck;
	public JLabel userHealth = new JLabel();
	public JLabel userRemainingCards = new JLabel();
	public JLabel userWcp = new JLabel();				//wildcat points (cost)
	public JLabel enemyHealth = new JLabel();
	public JLabel enemyRemainingCards = new JLabel();
	public JLabel enemyWcp = new JLabel();				//wildcat points (cost)
	public JLabel enemyHandLen = new JLabel();
	
	//panels of cards in player hand, change to array, later!!!!!!!!!!! -
	public CardPanel[] handCards = new CardPanel[10];
	public CardPanel[] userCards = new CardPanel[7];
	public CardPanel[] enemyCards = new CardPanel[7];
	
	
	//popup menus
	public JPopupMenu handMenu = new JPopupMenu();	//menu for hand
	public JPopupMenu userTableMenu = new JPopupMenu();	//menu for hand
	public JPopupMenu enemyTableMenu = new JPopupMenu();	//menu for hand
	
	
	public JMenuItem getHandDescription = new JMenuItem("Get Description");
	public JMenuItem playCard = new JMenuItem("Play Card");
	public JMenuItem getUTableDescription = new JMenuItem("Get Description");
	public JMenuItem getETableDescription = new JMenuItem("Get Description");
	public JMenuItem attkPlayer = new JMenuItem("Attack Player");
	public JMenu attkCard = new JMenu("Attack Card");
	public JMenuItem useAbilityOnYou = new JMenuItem("Use Ability On");
	public JMenuItem useAbilityOnEnemy = new JMenuItem("Use Ability On");
	//add ten items to submenu representing 10 possible cards
	public JMenuItem card0 = new JMenuItem("0");
	public JMenuItem card1 = new JMenuItem("1");
	public JMenuItem card2 = new JMenuItem("2");
	public JMenuItem card3 = new JMenuItem("3");
	public JMenuItem card4 = new JMenuItem("4");
	public JMenuItem card5 = new JMenuItem("5");
	public JMenuItem card6 = new JMenuItem("6");
	
	
	public int cIden;											//identifies the panel clicked
	
	public Socket gameSocket;
	public DataInputStream inData;
	public DataOutputStream outData;
	
	
	public String selfName=null;
	public String peerName=null;
	public String host=null;
	public int port=4331;
	public GameThread gamethread;
	
	public GamePad(ControlPad controlpad)
	{
		initCards();
		gamethread = new GameThread(this, controlpad);
		//board holds the cards
		
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
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
		
		userHand.setBackground(new Color(106,49,163));
		enemyTable.setBackground(new Color(106,49,163));
		userTable.setBackground(new Color(106,49,163));
		enemyHand.setBackground(new Color(106,49,163));
		
		
		//and other panels
		deckPanel.setLayout(new BoxLayout(deckPanel, BoxLayout.Y_AXIS));
		deckPanel.setBackground(new Color(106,49,163));
		deck = resizeImage("img/Card_Back.jpg", 75, 90);
		deckLabel = new JLabel(deck);
		deckPanel.add(deckLabel);
		userHealth.setText("Health: " + String.valueOf(userPlayer.getCredits()));
		deckPanel.add(userHealth);
		userWcp.setText("Wildcat Points: " + String.valueOf(userPlayer.getWcp()));
		deckPanel.add(userWcp);
		userRemainingCards.setText("Cards Left: " + userPlayer.getDeck().cardsLeft());
		deckPanel.add(userRemainingCards);
		userHand.add(deckPanel);
		
		
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
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
			userTableMenu.add(useAbilityOnYou);
			useAbilityOnYou.setEnabled(false);
			useAbilityOnEnemy.setEnabled(false);
			enemyTableMenu.add(getETableDescription);
			enemyTableMenu.add(useAbilityOnEnemy);
			
			//add ten items to submenu representing 10 possible cards
			attkCard.add(card0);
			attkCard.add(card1);
			attkCard.add(card2);
			attkCard.add(card3);
			attkCard.add(card4);
			attkCard.add(card5);
			attkCard.add(card6);
			
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
					if (e.getSource() == useAbilityOnEnemy)
					{
						executeOnTarget(savedCard, enemyCards[cIden].getRegCard());
					}
					if (e.getSource() == useAbilityOnYou)
					{
						executeOnTarget(savedCard, userCards[cIden].getRegCard());
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
			useAbilityOnEnemy.addMouseListener(cardListener);
			useAbilityOnYou.addMouseListener(cardListener);
			
			card0.addMouseListener(cardListener);
			card1.addMouseListener(cardListener);
			card2.addMouseListener(cardListener);
			card3.addMouseListener(cardListener);
			card4.addMouseListener(cardListener);
			card5.addMouseListener(cardListener);
			card6.addMouseListener(cardListener);
			
		}
		catch (Exception ex)
		{
			System.out.println("No file");
		}
		this.revalidate();
		this.repaint();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getHandDescription)
		{
			//JOptionPane.showMessageDialog(this, this, , null);
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
			System.out.println("test card 1");
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
		
		
		
	}
	
	
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
	
	
	
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		//for example, repaint the game pad
		
		System.out.println("aaaaaa");
	}
	@Override
	public void mouseReleased(MouseEvent e){}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	/**
	 * Retrieves information about cards from text file, creates actual cards used in the game.
	 */
	public void initCards()
	{
		yourName = JOptionPane.showInputDialog(userTable, "Name:", "Enter a name:", 1);
		yourName += "::";
		Random r = new Random();
		yourName += r.nextInt();
		yourName += r.nextInt();
		yourName += r.nextInt();
		yourName += r.nextInt();
		yourName += r.nextInt();
		
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
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		
		userPlayer = new Player(yourName, new DeckofCards(30, gameCards));			//test not working
		enemyPlayer = new Player("Eric", new DeckofCards(30, gameCards2));

		userPlayer.clearHand();
		userPlayer.addCardToHand(gameCards[20]);
		userPlayer.addCardToHand(gameCards[21]);
		userPlayer.addCardToHand(gameCards[19]);
		userPlayer.addCardToHand(gameCards[6]);
		userPlayer.addCardToHand(gameCards[40]);
		userPlayer.addCardToHand(gameCards[29]);
		userPlayer.addCardToHand(gameCards[30]);
		userPlayer.addCardToHand(gameCards[31]);
		userPlayer.addCardToHand(gameCards[33]);
		userPlayer.addCardToHand(gameCards[36]);
		
		
		
	}
	public void showPopup(MouseEvent e, String menu)
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
					if (secondClick == false)
						useAbilityOnYou.setEnabled(false);
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
					if (secondClick == false)
						useAbilityOnEnemy.setEnabled(false);
					break;
			}
		}
	}
	
			
	
	//show card's ability description
	public void getCardDescription(CardPanel card)
	{
		JOptionPane.showMessageDialog(userHand, (!card.getDescription().equals("") ? card.getDescription() : "Card has no ability"), "Description for " + card.getCardName(), 1, null);
	}
	
	//put card in hand onto board
	public void playCard(CardPanel card)
	{
		useAbilityOnEnemy.setEnabled(false);
		useAbilityOnYou.setEnabled(false);
		try {
			Card temp = userPlayer.playCard(cIden);
			if (temp instanceof AbilityCard)
			{
				executeEvent((AbilityCard) temp);
				gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
				//gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());		
				updatePlayerStats();
				updateBoardCards();
				updateHandCards();
			}
			else
			{
				if (temp.getAbility() != ability.NONE)
					executeReg((RegCard) temp);
				userPlayer.addCardtoBoard(temp);
			}
			gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
			//gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());				
			updateBoardCards();
			updateHandCards();
			updatePlayerStats();
			//chatWindow.append("GAME MESSAGE: " + userPlayer.getName() + " played: " + temp.getName() + "\n");	//would like to make game messages different color,
			//but this requires using JTextPane instead of editor, if someone would like to learn that...
			
			
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
	
	public void updateBoardCards()
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
	}
	
	public void attkPlayer(CardPanel cardPanel)
	{
		RegCard card = cardPanel.getRegCard();
		int attack = card.getAttack();
		enemyPlayer.setCredits(-attack);
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());
		updatePlayerStats();
		card.setHasAttacked(true);
	}
	
	public void attkCard(CardPanel attkPanel, CardPanel defPanel)
	{
		RegCard attacker = attkPanel.getRegCard();
		RegCard defender = defPanel.getRegCard();
		attacker.setHealth(attacker.getHealth()-defender.getAttack());
		defender.setHealth(defender.getHealth()-attacker.getAttack());
		attacker.setHasAttacked(true);
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());	
		updateBoardCards();
	}
	
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
		else
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
					
			}
			
		}
	}
	
	
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
		
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());			
		updatePlayerStats();
		updateHandCards();
		updateBoardCards();
	}
	
	//needs additional input
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
		gamethread.sendMessage("/" + peerName+ " /chess " + userPlayer.toString());
		gamethread.sendMessage("/" + peerName+ " /chess " + enemyPlayer.toString());		
		updateBoardCards();
		updateHandCards();
		updatePlayerStats();
	}
}

