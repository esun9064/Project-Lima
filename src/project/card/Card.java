
package project.card;

import java.awt.image.BufferedImage;

/**
 * Represents a single playing card.
 * @author Team Lima
 */
public class Card {
	
	private String name;
	private int cost;
	private BufferedImage image;
	

	public Card(String name, int cost, BufferedImage image)
	{
		this.name = name;
		this.cost = cost;
		this.image = image;
	}
	
}
