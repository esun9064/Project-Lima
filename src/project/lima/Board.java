/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import project.card.*;
import project.card.Card.ability;
/**
 *
 * @author Eric
 */



public class Board {
	Card[] gameCards = new Card[50];
	
	/**
	 * Retrieves information about cards from text file, creates actual cards used in the game.
	 */
	public void initCards()
	{
		//get regular card data
		try {
			Scanner scanner = new Scanner(new File("regularCards.txt"));
			for (int i = 0 ; i < 50; i ++)				//get regular cards
			{
				String next = scanner.nextLine();
				String[] data = next.split(",");
				String name = data[0];
				int cost = Integer.parseInt(data[1]);
				BufferedImage image = ImageIO.read(new File(data[3]));
				int attack = Integer.parseInt(data[4]);
				int health = Integer.parseInt(data[5]);
				String description = data[6];
				ability a = ability.valueOf(data[7]);
				gameCards[i] = new RegCard(name, cost, image, attack, health, description, a);
			}
			for (int i = 50 ; i < 100; i ++)				//get ability cards
			{
				scanner = new Scanner(new File("abilityCards.txt"));
				String next = scanner.nextLine();
				String[] data = next.split(",");
				String name = data[0];
				int cost = Integer.parseInt(data[1]);
				BufferedImage image = ImageIO.read(new File(data[3]));
				String description = data[4];
				ability a = ability.valueOf(data[5]);
				gameCards[i] = new AbilityCard(name, cost, image, description, a);
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		
	}
	
	
}
