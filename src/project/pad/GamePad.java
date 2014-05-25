package project.pad;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import project.card.*;
import project.card.Card.ability;
import project.deck.DeckofCards;

import project.lima.CardPanel;
import project.lima.Player;
import project.thread.*;

public class GamePad extends Panel implements MouseListener,ActionListener
{
    public static Card[] gameCards = new Card[50];
    protected static Player userPlayer = new Player("Eric", new DeckofCards(30, gameCards));			//test not working
    protected static Player enemyPlayer = new Player("Eric", new DeckofCards(30, gameCards));
    //panels containing the cards
    public Panel enemyHand = new Panel();
    public Panel enemyTable = new Panel();
    public Panel userHand = new Panel();
    public Panel userTable = new Panel();
    //holds deck and user info
    public Panel deckPanel = new Panel();
    public JLabel deckLabel = new JLabel();
    public ImageIcon deck;
    public JLabel userHealth = new JLabel();
    public JLabel userRemainingCards = new JLabel();
    public JLabel userWcp = new JLabel();				//wildcat points (cost)

    //  execute variables
    public static Card savedCard = null;
    public static boolean secondClick = false;
    
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
         gamethread=new GameThread(this, controlpad);    

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

        enemyHand.setPreferredSize(new Dimension(1000, 150));
        add(enemyHand, BorderLayout.EAST);


        enemyTable.setPreferredSize(new Dimension(1000, 150));
        add(enemyTable,BorderLayout.CENTER);



        userTable.setPreferredSize(new Dimension(1000, 150));
        add(userTable, BorderLayout.AFTER_LAST_LINE);


        userHand.setPreferredSize(new Dimension(1000, 150));
        add(userHand, BorderLayout.SOUTH);

                
                
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

        try {
                deckPanel.setLayout(new BoxLayout(deckPanel, BoxLayout.Y_AXIS));
                deck = resizeImage("img/Card_Back.jpg", 100, 100);
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
                
           
                        
                   //listener for card actions - attack, get description etc
        MouseListener cardListener = new MouseListener()
        {				
            @Override
            public void mouseClicked(MouseEvent e) {


            }


            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("bbbbbbbbbbbbbbbbbbbb");
                //Test send message,
                 gamethread.sendMessage("/"+peerName+" /chess "+"this a test message");
                 
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

        }catch (Exception ex)
        {
                System.out.println("No file");
        }
                
                
      
    }
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

        
        
      //show card's ability description
	public void getCardDescription(CardPanel card)
	{
		JOptionPane.showMessageDialog(userHand, (!card.getDescription().equals("") ? card.getDescription() : "Card has no ability"), "Description for " + card.getCardName(), 1, null);
	}
	
	//put card in hand onto board
	public void playCard(CardPanel card)
	{
		
	}  
        
        
        
        /**
             * Retrieves information about cards from text file, creates actual cards used in the game.
             */
            public static void initCards()
            {
                    //get regular card data
                    try {
                            Scanner scanner = new Scanner(new File("txt/Master_RegCard_List.txt"));
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

                            scanner = new Scanner(new File("txt/Master_AbilityCard_List"));
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
//                            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
               //             Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            
           public void executeReg(RegCard r)
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
            
          
            
            
        
        
        public void executeEvent(AbilityCard a)
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
                      break;
                }
              
        }
        }
        
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

  //helper method for resizing images
	public ImageIcon resizeImage(String filePath, int w, int h)
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

}

