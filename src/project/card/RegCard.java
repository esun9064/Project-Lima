/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.card;

import java.awt.image.BufferedImage;

/**
 *
 * @author Eric
 */
public class RegCard extends Card {
	
	private int attack;
	private int health;	
	
	public RegCard(String name, int cost, BufferedImage image, int attack, int health)
	{
		super(name, cost, image);
		this.attack = attack;
		this.health = health;
	}
}
