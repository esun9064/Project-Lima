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
	
        /**
         * Initializes a standard Ability Card
         * @param name the Ability Card's name
         * @param cost the Ability Card's cost, taken from the Player's wcp resource
         * @param image the String representing an Ability Card's image path
         * @param d  the description of the ability
         * @param a  the implementation of the ability enumerated type
         */
	public AbilityCard(String name, int cost, String image, String d, ability a)
	{
		super(name, cost, image, d, a);
		
	}
        /**
         * Prints an Ability Card's attributes out in the following order:
        * name, cost, image, description, ability
        * @return a nicely formatted String representing an Ability Card's attributes
        */
	@Override
	public String toString()
        {
			String s = ("ability,"+name + "," + cost + "," + imagePath + "," + description + "," + ability);
			return s;
        }
}
