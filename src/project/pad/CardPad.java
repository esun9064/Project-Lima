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
import project.card.AbilityCard;
import project.card.Card;
import project.card.RegCard;
import project.deck.DeckofCards;

import project.lima.Player;
import project.lima.CardPanel;
import project.thread.*;

public class CardPad extends JPanel implements MouseListener,ActionListener
{
 	public static Card[] gameCards = new Card[50];
	protected static Player userPlayer = new Player("Eric", new DeckofCards(30, gameCards));			//test not working
	protected static Player enemyPlayer = new Player("Eric", new DeckofCards(30, gameCards));
	
	
				//panel holds game board (aka cards on board and hand)
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
	

	
	
	public static int cIden;
        
        
        
 
 public boolean isMouseEnabled=false,isWin=false,isInGame=false;
 public TextField statusText=new TextField("Please connect to server first.");

 public Socket gameSocket;
 public DataInputStream inData;
 public DataOutputStream outData;

 public String selfName=null;
 public String peerName=null;
 public String host=null;
 public int port=4331;
 public CardThread gamethread=new CardThread(this);

    public CardPad()
    {
        /*
  setSize(440,440);
     setLayout(null);
     setBackground(Color.pink);
     addMouseListener(this);
     add(statusText);
     statusText.setBounds(40,5,360,24);
     statusText.setEditable(false);*/
        
        initCards();
		/*
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(1366, 720));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameArea.setLayout(new BorderLayout());
		frame.setContentPane(gameArea);*/
		//board holds the cards
		
		
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

                //board holds the cards
		GridBagConstraints c = new GridBagConstraints();
                
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
//		enemyHand.setLayout(new boxLayout(enemyHand, BoxLayout.Y_AXIS));
		
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
                                        /*
					//menubar items
					if (e.getSource() == quit)
					{
						frame.dispose();
					}*/
					
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
				
				
			}
                        catch (Exception ex)
                        {
                                System.out.println("No file");
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
    statusText.setText("chessPad:connectServer:Can not connect \n");
   }
   return false;
  }


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
				Card.ability a = Card.ability.valueOf(data[6]);
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
				Card.ability a = Card.ability.valueOf(data[4]);
				gameCards[i] = new AbilityCard(name, cost, image, description, a);
				i++;
			}
			
		} catch (FileNotFoundException ex) {
//			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
//			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        
         public void executeEvent(AbilityCard a)
        {
            Card.ability abil = a.getAbility();
            
            
            if ((abil == Card.ability.STlowerattackto1) || (abil == Card.ability.STdamage6))  // for the cards that need to select a card to operate on
            {
                
            }
            else
            {
              switch (abil)   // cards whose effect does not require a choice
              {
                  case OPdamage5:
                      enemyPlayer.setCredits(enemyPlayer.getCredits() - 5);
                      break;
                  case OPdamage14:
                      enemyPlayer.setCredits(enemyPlayer.getCredits() - 14);
                      break;
                  case OPdamage8:
                      enemyPlayer.setCredits(enemyPlayer.getCredits() - 8);
                      break;
                      /*
                  case AOEedamage1:
                      for (int i = 0; i < enemyCards.length; i++)
                          if (enemyCards[i].getCard() != null)
                          {
                          enemyCards[i].getCard().setHealth(enemyCards[i].getCard().getHealth() - 1);
                          }
                      break;
                  case AOEedamage3: 
                      for (int i = 0; i < enemyCards.length; i++)
                          if (enemyCards[i].getCard() != null)
                          {
                          enemyCards[i].getCard().setHealth(enemyCards[i].getCard().getHealth() - 3);
                          }
                      break;
                  case AOEfheal3:
                      for (int i = 0; i < userCards.length; i++)
                          if (userCards[i].getCard() != null)
                          {
                            int newHealth = userCards[i].getCard().getHealth() + 3;
                            if (newHealth > userCards[i].getCard().getMaxHealth())
                            {
                                userCards[i].getCard().setHealth(userCards[i].getCard().getMaxHealth());
                            }
                            else
                            {
                                userCards[i].getCard().setHealth(userCards[i].getCard().getHealth() + 3);
                            }
                          }
                      break;
                  case firedrill:
                      for (int i = 0; i < userCards.length; i++)
                      {
                         userPlayer.addCardtoHand(i);
                         userCards[i].removeCardFromPanel();
                       
                      }
                      for (int i = 0; i < enemyCards.length; i++)
                      {
                      enemyPlayer.addCardtoHand(i);
                      enemyCards[i].removeCardFromPanel();
                      }
                      break;*/
                }
              
        }
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
	
	
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

     public void getLocation(int a,int b,int color)
     {

  
  }


     public boolean checkWin(int a,int b,int checkColor)
     {
         return false;
    }

 

 

     public void paint(Graphics g)
     {
   
      }


      public void chessPaint(int chessPoint_a,int chessPoint_b,int color)
      {
   
   }


      public void netChessPaint(int chessPoint_a,int chessPoint_b,int color)
      {
    
   }


      public void mousePressed(MouseEvent e)
      {
   
  }

  public void mouseReleased(MouseEvent e){}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}

  public void actionPerformed(ActionEvent e)
  {

  }
}


class chessPoint_black extends Canvas implements MouseListener
{
 CardPad chesspad=null;
 chessPoint_black(CardPad p)
 {
  setSize(20,20);
  chesspad=p;
  addMouseListener(this);
 }

 public void paint(Graphics g)
 {
  g.setColor(Color.black);
  g.fillOval(0,0,14,14);
 }

 public void mousePressed(MouseEvent e)
 {
//  if(e.getModifiers()==InputEvent.BUTTON3_MASK)
//  {
//   cardpad.remove(this);
//   cardpad.chessColor=1;
//   cardpad.text_2.setText("");
//   cardpad.text_1.setText("���������");
//  }
 }
 public void mouseReleased(MouseEvent e){}
 public void mouseEntered(MouseEvent e) {}
 public void mouseExited(MouseEvent e) {}
 public void mouseClicked(MouseEvent e) {}
}


class chessPoint_white extends Canvas implements MouseListener
{
 CardPad chesspad=null;
 chessPoint_white(CardPad p)
 {
  setSize(20,20);
  addMouseListener(this);
  chesspad=p;
 }

 public void paint(Graphics g)
 {
  g.setColor(Color.white);
  g.fillOval(0,0,14,14);
 }

 public void mousePressed(MouseEvent e)
 {
//  if(e.getModifiers()==InputEvent.BUTTON3_MASK)
//  {
//   cardpad.remove(this);
//   cardpad.chessColor=-1;
//   cardpad.text_2.setText("���������");
//   cardpad.text_1.setText("");
//  }
 }
 public void mouseReleased(MouseEvent e){}
 public void mouseEntered(MouseEvent e) {}
 public void mouseExited(MouseEvent e) {}
 public void mouseClicked(MouseEvent e)
 {
//  if(e.getClickCount()>=2)
//  cardpad.remove(this);
 }
}

