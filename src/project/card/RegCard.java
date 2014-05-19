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
	
	public RegCard(String name, int cost, String image, int attack, int health, String d, ability a)
	{
		super(name, cost, image, d, a);
		this.attack = attack;
		this.health = health;
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
        
        public String toString(){
                String x;
        x = ("name: " + getName() + "\n cost: " + getCost() + 
                "\n image: " + getImage() + "\n attack: " + getAttack() + "\n health: " 
                + getHealth() + "\n description: " + getDesc() + "\n ability: " + getAbility());
            
            return x;
            
        }
            
        }

