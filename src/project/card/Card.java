
package project.card;

import java.awt.image.BufferedImage;

/**
 * Represents a single playing card.
 * @author Team Lima
 */
public class Card {
	
    public enum ability{damage, rethand, friendbuff, heal;}
    
	private String name;
	private int cost;
	private BufferedImage image;
        private String description;
	private ability ability;
        
        
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
}
        

