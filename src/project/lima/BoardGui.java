/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 *
 * @author Eric
 */
public class BoardGui {
	
	public static JPopupMenu attkMenu = new JPopupMenu();
	public static Card[] gameCards = new Card[50];
	
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
			/*
			scanner = new Scanner(new File("abilityCards.txt"));
			i = 0;
			while(scanner.hasNextLine())				//get ability cards
			{
				String next = scanner.nextLine();
				String[] data = next.split(",");
				String name = data[0];
				int cost = Integer.parseInt(data[1]);
				String image = data[3];
				String description = data[4];
				ability a = ability.valueOf(data[5]);
				gameCards[i] = new AbilityCard(name, cost, image, description, a);
				i++;
			}
			*/
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public static void showPopup(MouseEvent e)
	{
		if (e.isPopupTrigger())
		{
			attkMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}	
	
	public static void main(String[] args)
	{
		initCards();
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(1200, 720));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel board = new JPanel();
		frame.setContentPane(board);
		
		GridBagConstraints c = new GridBagConstraints();
		board.setLayout(new GridBagLayout());
		

		
		JPanel enemyHand = new JPanel();
		enemyHand.setBackground(Color.black);
		JPanel enemyTable = new JPanel();
		enemyTable.setBackground(Color.red);
		JPanel userHand = new JPanel();
		JPanel userTable = new JPanel();
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
		
		
		userHand.setLayout(new GridBagLayout());
		GridBagConstraints eH = new GridBagConstraints();
		eH.anchor = GridBagConstraints.NORTHWEST;
		eH.fill = GridBagConstraints.WEST;
		eH.weightx = 1;
		eH.insets = new Insets(0,0,0,0);
		eH.ipadx = 2;
		eH.gridx = 0;
		eH.gridy = 0;		
		try {
			ImageIcon magic = resizeImage("magic.jpg", 100, 100);
			JLabel magicLabel = new JLabel(magic);
			userHand.add(magicLabel, eH);
			CardPanel handCard0 = new CardPanel((RegCard)gameCards[0]);
			eH.gridx = 1;
			eH.weightx = .1;
			eH.gridy = 0;
			userHand.add(handCard0, eH);			
			CardPanel handCard1 = new CardPanel((RegCard)gameCards[1]);
			eH.gridx = 2;
			eH.gridy = 0;
			userHand.add(handCard1, eH);	
			CardPanel handCard2 = new CardPanel((RegCard)gameCards[2]);			
			eH.gridx = 3;
			eH.gridy = 0;
			userHand.add(handCard2, eH);
			CardPanel handCard3 = new CardPanel((RegCard)gameCards[3]);						
			eH.gridx = 4;
			eH.gridy = 0;
			userHand.add(handCard3, eH);				
			CardPanel handCard4 = new CardPanel((RegCard)gameCards[4]);						
			eH.gridx = 5;
			eH.gridy = 0;
			userHand.add(handCard4, eH);	
			CardPanel handCard5 = new CardPanel((RegCard)gameCards[5]);									
			eH.gridx = 6;
			eH.gridy = 0;
			userHand.add(handCard5, eH);	
			CardPanel handCard6 = new CardPanel((RegCard)gameCards[6]);												
			eH.gridx = 7;
			eH.gridy = 0;
			userHand.add(handCard6, eH);	
			CardPanel handCard7 = new CardPanel((RegCard)gameCards[7]);										
			eH.gridx = 8;
			eH.gridy = 0;
			userHand.add(handCard7, eH);			
			CardPanel handCard8 = new CardPanel((RegCard)gameCards[8]);										
			eH.gridx = 9;
			eH.gridy = 0;
			userHand.add(handCard8, eH);
			CardPanel handCard9 = new CardPanel((RegCard)gameCards[9]);													
			eH.gridx = 10;
			eH.gridy = 0;
			userHand.add(handCard9, eH);				
			userHand.revalidate();
			userHand.repaint();
			
			
			
			JMenuItem attkPlayer = new JMenuItem("Attack Player");
			attkMenu.add(attkPlayer);								//add attackPlayer item to menu
			JMenu attkCard = new JMenu("Attack Card");
			attkMenu.add(attkCard);
			//add ten items to submenu representing 10 possible cards
			JMenuItem card0 = new JMenuItem("0");
			attkCard.add(card0);
			JMenuItem card1 = new JMenuItem("1");
			attkCard.add(card1);			
			JMenuItem card2 = new JMenuItem("2");
			attkCard.add(card2);			
			JMenuItem card3 = new JMenuItem("3");
			attkCard.add(card3);			
			JMenuItem card4 = new JMenuItem("4");
			attkCard.add(card4);			
			JMenuItem card5 = new JMenuItem("5");
			attkCard.add(card5);			
			JMenuItem card6 = new JMenuItem("6");
			attkCard.add(card6);			
			
			
			
			MouseListener cardListener = new MouseListener()
			{

				@Override
				public void mouseClicked(MouseEvent e) {
					showPopup(e);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					showPopup(e);

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					showPopup(e);

				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}
				
			};
			
			handCard0.addMouseListener(cardListener);
			handCard1.addMouseListener(cardListener);
			handCard2.addMouseListener(cardListener);
			handCard3.addMouseListener(cardListener);
			handCard4.addMouseListener(cardListener);
			handCard5.addMouseListener(cardListener);

		
		
		
		
		}
		catch (Exception ex)
		{
			System.out.println("No file");
		}

	
	}
	
	public static ImageIcon resizeImage(String filePath, int w, int h) {


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
}
