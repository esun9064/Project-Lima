/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import project.card.AbilityCard;
import project.card.Card;
import project.card.RegCard;
import static project.lima.BoardGui.resizeImage;

/**
 *
 * @author Eric
 */
public class CardPanel extends JPanel{
	
	protected Card card = null;
	protected JLabel imgLbl = new JLabel();
	protected JLabel attackLbl = new JLabel();
	protected JLabel healthLbl = new JLabel();
	protected JLabel abilityLbl = new JLabel();
	protected JLabel costLbl = new JLabel();
	
	
	
	public CardPanel()
	{
		/*
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		this.card = null;
		add(imgLbl);
		add(attackLbl);
		add(healthLbl);
		add(abilityLbl);
		add(costLbl);
		*/
	}
	
	public CardPanel(Card card)
	{
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
		}
	}
	
	public String getDescription()
	{
		return this.card.getDesc();
	}
	
	public String getCardName()
	{
		return this.card.getName();
	}
	
	public void getNewCard(Card card)
	{
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
		}
	}
	
	public RegCard getRegCard() 
	{
		return (RegCard) card;
	}
	
	public AbilityCard getAbilityCard()
	{
		return (AbilityCard) card;
	}
	public void setAttack()
	{
		RegCard reg = (RegCard) this.card;
		attackLbl.setText(String.valueOf(reg.getAttack()));
		revalidate();
		repaint();
	}
	
	public void setHealth()
	{
		RegCard reg = (RegCard) this.card;
		healthLbl.setText(String.valueOf(reg.getHealth()));
		revalidate();
		repaint();
	}
	
	public void removeCardFromPanel()
	{
		attackLbl.setText("");
		healthLbl.setText("");
		imgLbl.setIcon(null);
		abilityLbl.setText("");
		card = null;			//possible error
		
	}
	
	public boolean isEmpty()
	{
		if (this.card == null)
		{
			return true;
		}
		else return false;
	}
}
