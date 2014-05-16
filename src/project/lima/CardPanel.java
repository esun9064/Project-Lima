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
	
	RegCard card;
	JLabel imgLbl;
	JLabel attackLbl;
	JLabel healthLbl;
	
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
	}
	
	public void getNewCard(RegCard card)
	{
		this.card = card;
		imgLbl = new JLabel(card.getImage());
		attackLbl.setText("Attack: " + this.card.getAttack());
		healthLbl.setText("Health: " + this.card.getHealth());	
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
		imgLbl = new JLabel();
		card = null;			//possible error
		
	}
}
