
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
        private String description;
	

	public Card(String name, int cost, BufferedImage image, String d)
	{
		this.name = name;
		this.cost = cost;
		this.image = image;
                this.description = d;
	}
	
}
