package project.lima;


import java.awt.Color;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import project.card.AbilityCard;
import project.card.Card;
import project.card.RegCard;
import project.pad.GamePad;

/**
 * Class used to represent individual cards in the GUI.
 * @author Eric
 */
public class CardPanel extends JPanel{
	
	protected Card card = null;						//card information
	protected JLabel imgLbl = new JLabel();			//image 
	protected JLabel attackLbl = new JLabel();		//attack
	protected JLabel healthLbl = new JLabel();		//health
	protected JLabel abilityLbl = new JLabel();		//ability
	protected JLabel costLbl = new JLabel();		//cost
	
	
	/**
	 * Constructs a new, empty CardPanel for the GUI with a purple
	 * background color.
	 */
	public CardPanel()
	{
		this.setBackground(new Color(106,49,163));
	}
	
	/**
	 * Constructs a new CardPanel to display a given Card on the GUI and
	 * initializes its attributes to correspond to the given Card.
	 * @param card the Card to be displayed
	 */
	public CardPanel(Card card)
	{
		this.setBackground(new Color(106,49,163));
		attackLbl.setForeground(GamePad.white);
		healthLbl.setForeground(GamePad.white);
		abilityLbl.setForeground(GamePad.white);
		costLbl.setForeground(GamePad.white);
		if (card instanceof AbilityCard)
		{
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.card = card;
			AbilityCard ab = (AbilityCard) this.card;
			imgLbl = new JLabel(card.getImage());
			add(imgLbl);
			abilityLbl = new JLabel("Ability: Yes");
			add(abilityLbl);
			costLbl = new JLabel("Cost: " + this.card.getCost());
			add(costLbl);
			revalidate();
			repaint();
		}
		else
		{
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.card = card;
			RegCard reg = (RegCard) this.card;
			imgLbl = new JLabel(card.getImage());
			add(imgLbl);
			attackLbl = new JLabel("Attack: " + reg.getAttack());
			add(attackLbl);
			healthLbl = new JLabel("Health: " + reg.getHealth());
			add(healthLbl);
			if (this.card.getDesc().equals(""))
			{
				abilityLbl = new JLabel("Ability: No");
			}
			else
				abilityLbl = new JLabel("Ability: Yes");
			add(abilityLbl);
			costLbl = new JLabel("Cost: " + this.card.getCost());
			add(costLbl);
			revalidate();
			repaint();
		}
	}
	
	/**
	 * Observer for getting the description of the Card represented by this
	 * CardPanel.
	 * @return the represented card's description as a String
	 */
	public String getDescription()
	{
		return this.card.getDesc();
	}
	
	/**
	 * Observer for getting the name of the Card represented by this CardPanel.
	 * @return  the Card's name as a String
	 */
	public String getCardName()
	{
		return this.card.getName();
	}
	
	/**
	 * Method to set the given card as the Card represented by this CardPanel
	 * and to reset all attributes of the CardPanel accordingly.
	 * @param card the new Card to be represented
	 */
	public void getNewCard(Card card)
	{
		this.setBackground(new Color(106,49,163));
		if (card instanceof AbilityCard)
		{
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.card = card;
			AbilityCard ab = (AbilityCard) this.card;
			imgLbl = new JLabel(card.getImage());
			add(imgLbl);
			abilityLbl = new JLabel("Ability: Yes");
			add(abilityLbl);
			costLbl = new JLabel("Cost: " + this.card.getCost());
			add(costLbl);
			attackLbl.setText("\n");
			healthLbl.setText("\n");
			add(attackLbl);
			add(healthLbl);
			revalidate();
			repaint();
		}
		else
		{
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.card = card;
			RegCard reg = (RegCard) this.card;
			imgLbl = new JLabel(card.getImage());
			add(imgLbl);
			attackLbl = new JLabel("Attack: " + reg.getAttack());
			add(attackLbl);
			healthLbl = new JLabel("Health: " + reg.getHealth());
			add(healthLbl);
			if (this.card.getDesc().equals(""))
			{
				abilityLbl = new JLabel("Ability: No");
			}
			else
				abilityLbl = new JLabel("Ability: Yes");
			add(abilityLbl);
			costLbl = new JLabel("Cost: " + this.card.getCost());
			add(costLbl);
			revalidate();
			repaint();
		}
		attackLbl.setForeground(GamePad.white);
		healthLbl.setForeground(GamePad.white);
		abilityLbl.setForeground(GamePad.white);
		costLbl.setForeground(GamePad.white);
	}
	
	/**
	 * Observer method to retrieve the Card currently represented by this CardPanel.
	 * @return the Card being represented
	 */
	public Card getCard()
	{
		return card;
	}
	
	/**
	 * Observer method to retrieve the Card currently represented by this
	 * CardPanel after casting it to a RegCard.
	 * @return  the Card represented, cast to a RegCard
	 */
	public RegCard getRegCard()
	{
		return (RegCard) card;
	}
	
	/**
	 * Method to reset this CardPanel's attackLbl attribute to match the attack
	 * of the Card it represents.
	 */
	public void setAttack()
	{
		RegCard reg = (RegCard) this.card;
		attackLbl.setText(String.valueOf(reg.getAttack()));
		revalidate();
		repaint();
	}
	
	/**
	 * Method to reset this CardPanel's healthLbl attribute to match the health
	 * of the Card it represents.
	 */
	public void setHealth()
	{
		RegCard reg = (RegCard) this.card;
		healthLbl.setText(String.valueOf(reg.getHealth()));
		revalidate();
		repaint();
	}
	
	/**
	 * Method to reset the CardPanel to be empty, with no displayed Card.
	 */
	public void removeCardFromPanel()
	{
		this.setBackground(new Color(106,49,163));
		attackLbl.setForeground(GamePad.white);
		healthLbl.setForeground(GamePad.white);
		abilityLbl.setForeground(GamePad.white);
		costLbl.setForeground(GamePad.white);
		attackLbl.setText("");
		healthLbl.setText("");
		imgLbl.setIcon(null);
		abilityLbl.setText("");
		costLbl.setText("");
		card = null;			//possible error
		revalidate();
		repaint();
		
	}
	
	/**
	 * Observer method to determine whether the CardPanel is empty.
	 * @return false if the CardPanel has been assigned a card to represent,
	 *          true otherwise
	 */
	public boolean isEmpty()
	{
		if (this.card == null)
		{
			return true;
		}
		else return false;
	}
}