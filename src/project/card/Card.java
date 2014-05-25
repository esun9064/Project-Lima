
package project.card;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import static project.lima.BoardGui.resizeImage;

/**
 * Represents a single playing card.
 * @author Team Lima
 */
public class Card {

    public static enum ability 
	{
		NONE(0), STdamage1(1), STdamage2(2), STdamage3(3), STdamage6(4),
    AOEdamage1(5), AOEedamage1(6), AOEedamage3(7), OPdamage5(8), OPdamage14(9),
    STreturnhand(10), STheal1(11), STheal2(12), STheal3(13), AOEfhealthbuff2(14), STattackbuff1(15),
    AOEfheal3(16), STlowerattackto1(17), STlowerattackby1(18), firedrill(19), destroyallfriendlies(20),
    selfdamage3(21), STincattack1(22), STincattack3(23), selfheal2(24), AOEfincdamage1(25), OPDamage9(26),
    AOEfheal1(27), selfheal10(28), george(29), selfdamage7(30), AOEfinchealth3(31), OPdamage8(32);
    ;
				
		final int id;
		ability (int id)
		{
			this.id = id;
		}
		
	}  // many types of card abilities
    //ST denotes single target
    //OP denotes an effect on the opposing player
    // aoe denotes area of effect attack
    // AOEf affects friendly cards
    //AOEe affects enemy cards
    //firedrill has its own unique ability, returning all cards to owners' hand.  just named firedrill here
    
	String name;   
	protected int cost;
	protected ImageIcon image;
    protected String description;   //description of ability
	protected ability ability;    // implementation of ability 
    protected boolean abilityUsed;
        
	public Card(String name, int cost, String image, String d, ability a)
	{
		this.name = name;
		this.cost = cost;
		this.image = resizeImage(image, 85, 100);
        this.description = d;
        this.ability = a;
		this.abilityUsed = false;
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
         
        public ImageIcon getImage(){
            return image;
        }
        
        public void setImage(ImageIcon i){
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
        
		public boolean isAbilityUsed()
		{
			return abilityUsed;
		}
		
		public void setAbilityUsed(boolean t)
		{
			abilityUsed = t;
		}
    
    @Override
        public String toString(){
            String x;
        x = ("name: " + name + "\n cost: " + cost + "\n image: " + image + "\n description: " + description + "\n ability: " + ability);
            
            return x;
            
        }
}
        

