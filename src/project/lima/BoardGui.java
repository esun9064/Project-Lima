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
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Eric
 */
public class BoardGui {
	
	static JPopupMenu attkMenu = new JPopupMenu();
	
	public static void showPopup(MouseEvent e)
	{
		if (e.isPopupTrigger())
		{
			attkMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}	
	
	public static void main(String[] args)
	{
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
			ImageIcon allison = resizeImage("src/project/214IMAGES/Allison Hall.jpeg", 100, 100);
			JLabel allisonLabel = new JLabel(allison);
			eH.gridx = 1;
			eH.weightx = .1;
			eH.gridy = 0;
			userHand.add(allisonLabel, eH);			
			ImageIcon lakefill = resizeImage("src/project/214IMAGES/Lakefill Goose.jpeg", 100, 100);
			JLabel lakefillLabel = new JLabel(lakefill);
			eH.gridx = 2;
			eH.gridy = 0;
			userHand.add(lakefillLabel, eH);	
			ImageIcon burger = resizeImage("src/project/214IMAGES/Burger King.JPG", 100, 100);
			JLabel burgerLabel = new JLabel(burger);
			eH.gridx = 3;
			eH.gridy = 0;
			userHand.add(burgerLabel, eH);			
			ImageIcon beach = resizeImage("src/project/214IMAGES/Beach Day.jpeg", 100, 100);
			JLabel beachLabel = new JLabel(beach);
			eH.gridx = 4;
			eH.gridy = 0;
			userHand.add(beachLabel, eH);				
			ImageIcon tech = resizeImage("src/project/214IMAGES/Tech.jpeg", 100, 100);
			JLabel techLabel = new JLabel(tech);
			eH.gridx = 5;
			eH.gridy = 0;
			userHand.add(techLabel, eH);				
			ImageIcon fire = resizeImage("src/project/214IMAGES/Fire Drill.jpeg", 100, 100);
			JLabel fireLabel = new JLabel(fire);
			fireLabel.setIcon(fire);
			eH.gridx = 6;
			eH.gridy = 0;
			userHand.add(fireLabel, eH);				
			ImageIcon midterm = resizeImage("src/project/214IMAGES/EECS Midterm.jpeg", 100, 100);
			JLabel midtermLabel = new JLabel(midterm);
			eH.gridx = 7;
			eH.gridy = 0;
			userHand.add(midtermLabel, eH);	
			ImageIcon spac = resizeImage("src/project/214IMAGES/SPAC.jpeg", 100, 100);
			JLabel spacLabel = new JLabel(spac);
			eH.gridx = 8;
			eH.gridy = 0;
			userHand.add(spacLabel, eH);			
			ImageIcon rock = resizeImage("src/project/214IMAGES/The Rock.jpg", 100, 100);
			JLabel rockLabel = new JLabel(rock);
			eH.gridx = 9;
			eH.gridy = 0;
			userHand.add(rockLabel, eH);			
			ImageIcon kellogg = resizeImage("src/project/214IMAGES/Kellogg.jpeg", 100, 100);
			JLabel kelloggLabel = new JLabel(kellogg);
			eH.gridx = 10;
			eH.gridy = 0;
			userHand.add(kelloggLabel, eH);				
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
			JMenuItem card7 = new JMenuItem("7");
			attkCard.add(card7);			
			JMenuItem card8 = new JMenuItem("8");
			attkCard.add(card8);			
			JMenuItem card9 = new JMenuItem("9");
			attkCard.add(card9);
			
					
			
			
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
			
			spacLabel.addMouseListener(cardListener);
			kelloggLabel.addMouseListener(cardListener);
			burgerLabel.addMouseListener(cardListener);
			lakefillLabel.addMouseListener(cardListener);
			beachLabel.addMouseListener(cardListener);
			allisonLabel.addMouseListener(cardListener);

		
		
		
		
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
            System.out.println("This image can not be resized. Please check the path and type of file.");
            return null;
        }

     }
}
