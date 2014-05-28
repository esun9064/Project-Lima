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
	private final int initwcp = 500;
	protected boolean isFirst;
	
	public Player(String name, DeckofCards deck)
	{
		this.name = name;
		this.deck = deck;
		currentCredits = maxCredits;
		wcp = initwcp;
	}
	
	public void initHand()
	{
		for (int i = 0; i < 10 ; i++)
		{
			hand.add(this.deck.dealCard());
		}
	}
	
	public String getName() 
	{
		return this.name;
	}
	
	public void setName(String s)
	{
		this.name = s;
	}
	
	public DeckofCards getDeck()
	{
		return this.deck;
	}
	
	public ArrayList<Card> getHand()
	{
		return this.hand;		
	}
	
		
	public ArrayList<Card> getBoard()
	{
		return this.board;
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
			
		if (board.size() < 7)
			if (hand.get(index).getCost() <= wcp)
			{
				wcp -= hand.get(index).getCost();
				Card temp = hand.get(index);
				hand.remove(index);
				return temp;
			}
			else
				throw new MuchCostException("Card cost too much!");
		else 
			throw new MuchCardsException("You can only play 7 cards on the board!");
				
	}
	
	public void addCardtoHand(int index)
	{
		if (hand.size() < 10)
			hand.add(board.get(index));
		board.remove(index);
	}
	
	public void addCardtoBoard(Card card)
	{
		if (board.size() < 7)
			board.add(card);
	}

	public void removeCardFromBoard(int index)
	{
		board.remove(index);
	}
	
	public void removeCardFromBoard(Card card)
	{
		int length = board.size();
		for (int i = 0; i < length; i++)
		{
			if (board.get(i) == card)
			{
				board.remove(i);
				return;
			}
		}
	}	
	public void addCardToHand(Card card)
	{
		if (hand.size() < 10)
			hand.add(card);
	}
	
	public void clearHand()
	{
		int h = hand.size() -1;
		for (int i = h ; i >= 0; i--)
		{
			this.hand.remove(i);
		}
	}
	//player
	//hand
	//board
	//deck
	public String toString()
	{
		String string = this.getName() + ";" + this.getCredits() + ";" + this.getWcp() + "\n";
		for (int i = 0 ; i < hand.size() -1; i++)
			string += hand.get(i).toString() + ";";
		string += hand.get(hand.size() - 1) + "\n";
		for (int i = 0 ; i < board.size() -1; i++)
			string += board.get(i).toString() + ";";
		string += board.get(board.size() - 1) + "\n";
		string += deck.getNumDealt();
		return string;
	}
}
