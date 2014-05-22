/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import project.card.Card;
import project.card.RegCard;
import static project.lima.BoardGui.resizeImage;

/**
 *
 * @author Eric
 */
public class CardPanel extends JPanel{
	
	protected RegCard card;
	protected JLabel imgLbl;
	protected JLabel attackLbl;
	protected JLabel healthLbl;
	protected JLabel abilityLbl;
	protected JLabel costLbl;
	
	
	
	public CardPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));		
		this.card = null;
		imgLbl = new JLabel();
		attackLbl = new JLabel();
		healthLbl = new JLabel();
		abilityLbl = new JLabel();
		costLbl = new JLabel();
		add(imgLbl);
		add(attackLbl);
		add(healthLbl);
		add(abilityLbl);
		add(costLbl);
	}
	
	public CardPanel(RegCard card)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.card = card;
		imgLbl = new JLabel(card.getImage());
		add(imgLbl);
		attackLbl = new JLabel("Attack: " + this.card.getAttack());
		add(attackLbl);
		healthLbl = new JLabel("Health: " + this.card.getHealth());	
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
	
	public void getNewCard(RegCard card)
	{
		this.card = card;
		imgLbl = new JLabel(card.getImage());
		attackLbl.setText("Attack: " + this.card.getAttack());
		healthLbl.setText("Health: " + this.card.getHealth());
		if (!this.card.getDesc().equals(""))
		{
			abilityLbl.setText("Ability: Yes");
		}
		else
			abilityLbl.setText("Ability: No");

		revalidate();
		repaint();
	}
	
	public Card getCard() {
		return card;
	}
	
	public void setAttack()
	{
		attackLbl.setText(String.valueOf(this.card.getAttack()));
		revalidate();
		repaint();
	}
	
	public void setHealth()
	{
		healthLbl.setText(String.valueOf(this.card.getHealth()));
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
