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
	
	protected int attack;
	protected int health;	
	protected int maxattack = attack;
	protected int maxhealth = health;
	protected boolean hasAttacked;
        /**
         * Default constructor for a RegCard
         * @param name the RegCard's name
         * @param cost the RegCard's cost in Player wcp
         * @param image the RegCard's image 
         * @param attack the attack value of the RegCard, also initializes maxattack
         * @param health the health value of the RegCard, also initializes maxhealth
         * @param d the description of the ability for RegCard
         * @param a the implementation of the RegCard's ability through an enumerated type ability
         */
	public RegCard(String name, int cost, String image, int attack, int health, String d, ability a)
	{
		super(name, cost, image, d, a);
		this.attack = attack;
		this.health = health;
        hasAttacked = false;
		maxhealth = this.health;
		maxattack = this.attack;
	}
	/**
         * Constructor used for serialization
         * @param name the RegCard's name
         * @param cost the RegCard's cost in Player wcp
         * @param image the RegCard's image 
         * @param attack the attack value of the RegCard, 
         * @param health the health value of the RegCard,
         * @param d the description of the ability for RegCard
         * @param a the implementation of the RegCard's ability through an enumerated type ability
         * @param maxattack the maximum attack of this RegCard
         * @param maxhealth the maximum health of this RegCard
         */
	public RegCard(String name, int cost, String image, int attack, int health, String d, ability a, int maxattack, int maxhealth)
	{
		super(name, cost, image, d, a);
		this.attack = attack;
		this.health = health;
		this.maxhealth = maxhealth;
		this.maxattack = maxattack;
	}
        /**
         * Observer method that gets a RegCard's attack
         * @return the attack value of this RegCard
         */
        public int getAttack(){
            return attack;
        }
        /**
         * Transformer method that sets a RegCard's attack to a specified integer value
         * @param at the integer value that a RegCard's attack will be set to
         */
        public void setAttack(int at){
            this.attack = at;
        }
        /**
         * Observer method that gets a RegCard's health
         * @return the health value of this RegCard
         */
        public int getHealth(){
            return health;
        }
        /**
         * Transformer method that sets a RegCard's health to a specified integer value
         * @param h the integer value that a RegCard's health will be set to
         */
        public void setHealth(int h){
            this.health = h;
        }
        /**
         * Observer method that gets a RegCard's maximum health
         * @return the maxhealth value of this RegCard
         */
        public int getMaxHealth(){
            return maxhealth;
        }
        /**
         * Transformer method that sets a RegCard's maximum attack to a specified integer value
         * @param h the integer value that a RegCard's maxattack will be set to
         */
        public int getMaxAttack(){
            return maxattack;
        }
        /**
         * Observer method determining whether a Card has attacked yet
         * @return a boolean value that is true if a card has attacked
         */
		public boolean hasAttacked()
		{
			return hasAttacked;
		}
		/**
                 * Transformer method that sets hasAttacked to a specified boolean
                 * @param t the boolean value to which hasAttacked shall be changed to
                 */
		public void setHasAttacked(boolean t)
		{
			hasAttacked = t;
		}
	/**
     * Prints a RegCard's attributes out in the following order:
     * name, cost, image, , attack, health, description, ability, maxattack, maxhealth
     * @return a nicely formatted String representing a Card's attributes
     */
		@Override
		public String toString()
		{
			String s = "reg:" + name + ":";
			s += cost + ":";
			s += imagePath + ":";
			s += attack + ":";
			s += health + ":";
			s += description + ":";
			s += ability + ":";
			s += maxattack + ":" + maxhealth;
			return s;
		}
            
}

