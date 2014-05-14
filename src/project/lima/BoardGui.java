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
		enemyHand.setBackground(Color.red);
		JPanel enemyTable = new JPanel();
		enemyTable.setBackground(Color.blue);
		JPanel userHand = new JPanel();
		JPanel userTable = new JPanel();
		userTable.setBackground(Color.green);
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
			ImageIcon stannis = resizeImage("stannis.jpg", 100, 100);
			JLabel stannisLabel = new JLabel(stannis);
			eH.gridx = 1;
			eH.weightx = .1;
			eH.gridy = 0;
			userHand.add(stannisLabel, eH);			
			ImageIcon ned = resizeImage("ned.jpg", 100, 100);
			JLabel nedLabel = new JLabel(ned);
			eH.gridx = 2;
			eH.gridy = 0;
			userHand.add(nedLabel, eH);	
			ImageIcon jaime = resizeImage("jaime.jpg", 100, 100);
			JLabel jaimeLabel = new JLabel(jaime);
			eH.gridx = 3;
			eH.gridy = 0;
			userHand.add(jaimeLabel, eH);			
			ImageIcon tyrion = resizeImage("tyrion.jpg", 100, 100);
			JLabel tyrionLabel = new JLabel(tyrion);
			eH.gridx = 4;
			eH.gridy = 0;
			userHand.add(tyrionLabel, eH);				
			ImageIcon daenerys = resizeImage("daenerys.jpg", 100, 100);
			JLabel daenerysLabel = new JLabel(daenerys);
			eH.gridx = 5;
			eH.gridy = 0;
			userHand.add(daenerysLabel, eH);				
			ImageIcon jon = resizeImage("jon.jpg", 100, 100);
			JLabel jonLabel = new JLabel(jon);
			jonLabel.setIcon(jon);
			eH.gridx = 6;
			eH.gridy = 0;
			userHand.add(jonLabel, eH);				
			ImageIcon littlefinger = resizeImage("littlefinger.jpg", 100, 100);
			JLabel littlefingerLabel = new JLabel(littlefinger);
			eH.gridx = 7;
			eH.gridy = 0;
			userHand.add(littlefingerLabel, eH);	
			ImageIcon hound = resizeImage("hound.jpg", 100, 100);
			JLabel houndLabel = new JLabel(hound);
			eH.gridx = 8;
			eH.gridy = 0;
			userHand.add(houndLabel, eH);			
			ImageIcon cersei = resizeImage("cersei.jpg", 100, 100);
			JLabel cerseiLabel = new JLabel(cersei);
			eH.gridx = 9;
			eH.gridy = 0;
			userHand.add(cerseiLabel, eH);			
			ImageIcon ironthrone = resizeImage("ironthrone.jpg", 100, 100);
			JLabel ironthroneLabel = new JLabel(ironthrone);
			eH.gridx = 10;
			eH.gridy = 0;
			userHand.add(ironthroneLabel, eH);				
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
			
			houndLabel.addMouseListener(cardListener);

		
		
		
		
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
