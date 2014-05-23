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
	
	public CardPanel(AbilityCard card)
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
	
	//overload RegCard
	public CardPanel(RegCard card)
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
	
	public String getDescription()
	{
		return this.card.getDesc();
	}
	
	public String getCardName()
	{
		return this.card.getName();
	}
	
	public void getNewCard(AbilityCard card)
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
	
	public void getNewCard(RegCard card)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.card = card;
		RegCard reg = (RegCard) this.card;
		imgLbl = new JLabel(card.getImage());
		add(imgLbl);		
		attackLbl.setText("Attack: " + reg.getAttack());
		add(attackLbl);
		healthLbl.setText("Health: " + reg.getHealth());
		add(healthLbl);
		if (!this.card.getDesc().equals(""))
		{
			abilityLbl.setText("Ability: Yes");
		}
		else
			abilityLbl.setText("Ability: No");
		add(abilityLbl);
		costLbl.setText("Cost: " + reg.getCost());
		revalidate();
		repaint();
	}
	
	public RegCard getCard() {
		return card;
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
