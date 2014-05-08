/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;

import java.util.ArrayList;
import project.deck.DeckofCards;
import project.card.Card;
import project.support.*;

/**
 *
 * @author Team Lima
 */
public class Player {
	
	private String name;
	private DeckofCards deck;
	private ArrayList<Card> hand = new ArrayList<Card>(10);
	private ArrayList<Card> board = new ArrayList<Card>(7);
	private int currentCredits;
	private final int maxCredits = 50;
	private int wcp;
	
	public String getName() 
	{
		return this.name;
	}
	
	public DeckofCards getDeck()
	{
		return this.deck;
	}
	
	public ArrayList<Card> getHand()
	{
		return this.hand;		
	}
	
	public int getCredits()
	{
		return this.currentCredits;
	}
	
	public int getMaxCredits()
	{
		return this.maxCredits;
	}
		
	public int getWcp()
	{
		return this.wcp;
	}
	
	public void setCredits(int credit)
	{
		if(currentCredits + credit > maxCredits)
			this.currentCredits = maxCredits;
		else if (currentCredits + credit <= 0)
			this.currentCredits = 0;
		else
			this.currentCredits += credit;
	}
	
	public void setWcp(int wcp)
	{
		this.wcp = wcp;
	}
	
	public void draw()
	{
		if (hand.size() < 10)
			hand.add(deck.dealCard());
	}
	
	public Card playCard(int index) throws RuntimeException
	{
		//int length = hand.size();
		//for (int i = 0 ; i < length ; i++)
		//{
		//	if (hand.get(i).getName().equals(name))
			
		if (hand.get(index).getCost() <= wcp)
		{
			wcp -= hand.get(index).getCost();
			Card temp = hand.get(index);
			hand.remove(index);
			return temp;
		}
		else
			throw new MuchCostException("Card cost too much!");
			
				
	}
	
	public void addCardtoHand(int index)
	{
		if (hand.size() <= 10)
			hand.add(board.get(index));
		board.remove(index);
	}
	
	public void addCardtoBoard(Card card)
	{
		if (board.size() <= 7)
			board.add(card);
	}

	
}
