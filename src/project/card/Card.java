
package project.card;

import javax.swing.ImageIcon;
import project.pad.GamePad;;
/**
 * Represents a single playing card.
 * @author Team Lima
 */
public class Card {
	
	/**
	 * Enumerated type that lists the many types of Card abilities.
	 * ST denotes single target,
	 * AOE denotes area of effect,
	 * AOEe denotes area of effect on opposing targets,
	 * AOEf denotes area of effect on friendly targets,
	 * selfdamage subtracts credits from the player who played this card,
	 * OPdamage refers to an ability that subtracts credits from the opposing player
	 * incattack increases attack, inchealth increases health
	 * returnhand denotes that this ability returns a card to a player's hand
	 * abilities such as firedrill and destroyallfriendlies are unique and are noted more specifically
	 */
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
		
	}
	
	String name;					//name of the card
	protected int cost;				//cost to play the card
	protected String imagePath;		//path where image is stored	
	protected ImageIcon image;		//actual image
	protected String description;   //description of ability
	protected ability ability;		// implementation of ability using enumerated type ability
	protected boolean abilityUsed;  // returns true if an ability has been used by a card
	
	/**
	 * Initializes a standard Card, initializes global variable abilityUsed to false
	 * @param name the Card's name
	 * @param cost the Card's cost, taken from the Player's wcp resource
	 * @param image the String representing a Card's image path
	 * @param d  the description of the ability
	 * @param a  the implementation of the ability enumerated type
	 */
	public Card(String name, int cost, String image, String d, ability a)
	{
		this.name = name;
		this.cost = cost;
		this.image = GamePad.resizeImage(image, 75, 90);
		this.description = d;
		this.ability = a;
		this.abilityUsed = false;
		this.imagePath = image;
	}
	
	/**
	 * Observer for getting a Card's name
	 * @return the name of this Card
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Transformer that sets a Card's name to a specified String
	 * @param name the String that a Card's name will be set to
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Observer for getting a Card's cost
	 * @return the cost of this Card
	 */
	public int getCost(){
		return cost;
	}
	
	/**
	 * Transformer that sets a Card's cost to a specified integer value
	 * @param cost the integer that a Card's cost will be set to
	 */
	public void setCost(int cost){
		this.cost = cost;
	}
	
	/**
	 * Observer for getting a Card's image
	 * @return the ImageIcon that is storing the image of this Card
	 */
	public ImageIcon getImage(){
		return image;
	}
	
	/**
	 * Transformer that sets a Card's image to a specified ImageIcon
	 * @param i the ImageIcon that a Card's image will be set to
	 */
	public void setImage(ImageIcon i){
		this.image = i;
	}
	/**
	 * Observer for getting a Card's description
	 * @return the description of this Card
	 */
	public String getDesc(){
		return description;
	}
	
	/**
	 * Transformer that sets a Card's description to a specified String
	 * @param d the String that a Card's description will be set to
	 */
	public void setDesc(String d){
		this.description = d;
	}
	
	/**
	 * Observer for getting a Card's ability
	 * @return the ability of this Card
	 */
	public ability getAbility(){
		return ability;
	}
	
	/**
	 * Transformer that sets a Card's ability to a specified ability
	 * @param a the ability that a Card's ability will be set to
	 */
	public void setAbility(ability a){
		this.ability = a;
	}
	
	/**
	 * Observer function that returns whether or not an ability has been used yet
	 * @return true if abilityUsed is true
	 */
	public boolean isAbilityUsed()
	{
		return abilityUsed;
	}
	
	/**
	 * Transformer function that alters abilityUsed,
	 * @param t a boolean value that abilityUsed will be changed to
	 */
	public void setAbilityUsed(boolean t)
	{
		abilityUsed = t;
	}
	
	/**
	 * Prints a Card's attributes out in the following order:
	 * name, cost, image, description, ability
	 * @return a nicely formatted String representing a Card's attributes
	 */
	@Override
	public String toString(){
		String x;
		x = ("name: " + name + "\n cost: " + cost + "\n image: " + image + "\n description: " + description + "\n ability: " + ability);
		return x;
	}
}