
package project.card;

import java.awt.image.BufferedImage;

/**
 * Represents a single playing card.
 * @author Team Lima
 */
public class Card {
	
    public enum ability{STdamage1, STdamage2, STdamage3, STdamage6, 
    AOEdamage1, AOEedamage1, AOEedamage3, OPdamage5, OPdamage12, 
    STreturnhand, STheal1, STheal2, STheal3, AOEfhealthbuff2, STattackbuff1, 
    AOEfheal3, STlowerattackto1, STlowerattackby1, firedrill, none}  // many types of card abilities
    //ST denotes single target
    //OP denotes an effect on the opposing player
    // aoe denotes area of effect attack
    // AOEf affects friendly cards
    //AOEe affects enemy cards
    //firedrill has its own unique ability, returning all cards to owners' hand.  just named firedrill here
    
	String name;   
	private int cost;
	private BufferedImage image;
        private String description;   //description of ability
	private ability ability;    // implementation of ability 
        
        
	public Card(String name, int cost, BufferedImage image, String d, ability a)
	{
		this.name = name;
		this.cost = cost;
		this.image = image;
                this.description = d;
                this.ability = a;
	}
	
        // Getters and Setters
        public String getName(){
            return name;
        }
        
        public void setName(String n){
            this.name = n;  
        }
        
        
        public int getCost(){
            return cost;
        }
        
        public void setCost(int c){
            this.cost = c;
        }    
         
        public BufferedImage getImage(){
            return image;
        }
        
        public void setImage(BufferedImage i){
            this.image = i;
        }  
        
        public String getDesc(){
            return description;
        }
        
        public void setDesc(String d){
            this.description = d;
        } 
        
        public ability getAbility(){
            return ability;
        }
        
        public void setAbility(ability a){
            this.ability = a;
        }
        
    
    @Override
        public String toString(){
            String x;
        x = ("name: " + name + "\n cost: " + cost + "\n image: " + image + "\n description: " + description + "\n ability: " + ability);
            
            return x;
            
        }
}
        

