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
public class AbilityCard extends Card {
	
	private int ability;

	public AbilityCard(String name, int cost, BufferedImage image, int ability)
	{
		super(name, cost, image);
		this.ability = ability;
	}
}
