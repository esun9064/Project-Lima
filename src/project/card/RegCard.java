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
        
	public RegCard(String name, int cost, String image, int attack, int health, String d, ability a)
	{
		super(name, cost, image, d, a);
		this.attack = attack;
		this.health = health;
        hasAttacked = false;
		maxhealth = this.health;
		maxattack = this.attack;
	}
	
	public RegCard(String name, int cost, String image, int attack, int health, String d, ability a, int maxattack, int maxhealth)
	{
		super(name, cost, image, d, a);
		this.attack = attack;
		this.health = health;
		this.maxhealth = maxhealth;
		this.maxattack = maxattack;
	}
        
        public int getAttack(){
            return attack;
        }
        
        public void setAttack(int at){
            this.attack = at;
        }
        
        public int getHealth(){
            return health;
        }
        
        public void setHealth(int h){
            this.health = h;
        }
        
        public int getMaxHealth(){
            return maxhealth;
        }
        
        public int getMaxAttack(){
            return maxattack;
        }
        
		public boolean hasAttacked()
		{
			return hasAttacked;
		}
		
		public void setHasAttacked(boolean t)
		{
			hasAttacked = t;
		}
	public String toString()
        {
            return ("reg," + name + "," + cost + "," + image + "," + attack + "," + health + "," + ability + "," + maxattack + "," + maxhealth);
        }
            
}

