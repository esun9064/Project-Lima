/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package project.lima;

import java.util.ArrayList;
import project.card.Card;
import project.card.RegCard;
import project.deck.DeckofCards;
import project.support.*;

/**
 * Class represents a player of the game. 
 * @author Team Lima
 */
public class Player {
	
	private String name;										
	private DeckofCards deck;										//player deck
	private ArrayList<Card> hand = new ArrayList<Card>(10);			//player's cards in hand
	private ArrayList<Card> board = new ArrayList<Card>(7);			//player's cards on the board
	private int currentCredits;				
	private final int maxCredits = 50;								//starting health
	private int wcp;
	private final int initwcp = 5;									//cost pts per turn
	protected boolean isFirst = false;								//check if player gets to go first
	
	/**
	 * Constructs a new Player given a name and a DeckofCards
	 * @param name  String containing the new player's name
	 * @param deck  DeckofCards to be assigned to this player
	 */
	public Player(String name, DeckofCards deck)
	{
		this.name = name;
		this.deck = deck;
		currentCredits = maxCredits;
		wcp = initwcp;
	}
	
	/**
	 * initialize hand to hold first 10 cards from the player's deck
	 */
	public void initHand()
	{
		for (int i = 0; i < 10 ; i++)
		{
			hand.add(this.deck.dealCard());
		}
	}
	
	/**
	 * Observer function to retrieve this Player's name
	 * @return  a String containing this Player's name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Method to change the player's name.
	 * @param s a String containing the new name to be set
	 */
	public void setName(String s)
	{
		this.name = s;
	}
	
	/**
	 * Observer method to retrieve this player's deck
	 * @return the DeckofCards representing this Player's deck
	 */
	public DeckofCards getDeck()
	{
		return this.deck;
	}
	
	/**
	 * set the player's deck variable numDealt to integer value nd
	 * @param nd
	 */
	public void setDeckND(int nd)
	{
		this.deck.setNumDealt(nd);
	}
	
	/**
	 * Observer method to retrieve this Player's hand.
	 * @return ArrayList containing the Cards in the Player's hand
	 */
	public ArrayList<Card> getHand()
	{
		return this.hand;
	}
	
	/**
	 * Observer method to retrieve this Player's cards on the board
	 * @return ArrayList containing the Cards on the Player's side of the board.
	 */
	public ArrayList<Card> getBoard()
	{
		return this.board;
	}
	
	/**
	 * Observer method to determine the Player's current number of credits.
	 * @return value representing the player's number of credits
	 */
	public int getCredits()
	{
		return this.currentCredits;
	}
	/**
	 * Observer method to retrieve this Player's maximum possible credits.
	 * @return value representing this player's max credits.
	 */
	public int getMaxCredits()
	{
		return this.maxCredits;
	}
	
	/**
	 * Observer method for player Wcp.
	 * @return value representing current Wcp
	 */
	public int getWcp()
	{
		return this.wcp;
	}
	
	/**
	 * this method modifies by the players health by adding integer value credit to its current health
	 * @param credit the number to be added to the player's current credits
	 */
	public void setCredits(int credit)
	{
		if(currentCredits + credit > maxCredits)
			this.currentCredits = maxCredits;
		else if (currentCredits + credit <= 0)
			this.currentCredits = 0;
		else
			this.currentCredits += credit;
	}
	
	/**
	 * Method to modify player health by directly replacing the current credits
	 * with either a new int value or the player's maximum credits, whichever
	 * is smaller.
	 * @param credits int representing the desired new value for this Player's credits.
	 */
	public void setNewCredits(int credits)
	{
		if (credits > maxCredits)
		{
			this.currentCredits = maxCredits;
		}
		else
			this.currentCredits = credits;
	}
	
	/**
	 * Sets the player's wcp.
	 * @param wcp number of wcp after method call
	 */
	public void setWcp(int wcp)
	{
		this.wcp = wcp;
	}
	
	/**
	 * draw a card from the deck into the hand, as long as the hand does not exceed its card limit of 10 cards
	 */
	public void draw()
	{
		if (hand.size() < 10)
			hand.add(deck.dealCard());
	}
	
	/**
	 * plays a card from the hand on to the board, as long as the board does not exceed its card limit of 7 cards
	 * modifies the player's Wildcat Points resource accordingly
	 * @param index the array index of the card in the player's hand
	 * @return the card that was played
	 * @throws RuntimeException is thrown if a card costs too much or if the board array is already full
	 */
	public Card playCard(int index) throws RuntimeException
	{
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
	
	/**
	 * add card to hand from the board, used in abilities like STreturnhand
	 * @param index the array index of the card on the board that needs to be added to the hand
	 */
	public void addCardtoHand(int index)
	{
		if (hand.size() < 10)
			hand.add(board.get(index));
		board.remove(index);
	}
	
	/**
	 * adds a card to board, doesn't necessarily need to be in a player's hand
	 * @param card the card to add to the board
	 */
	public void addCardtoBoard(Card card)
	{
		if (board.size() < 7)
			board.add(card);
	}
	
	/**
	 * removes a card from the board based on array index
	 * @param index the array index of the card to be removed from the board
	 */
	public void removeCardFromBoard(int index)
	{
		board.remove(index);
	}
	
	/**
	 * removes a card from the board by searching for that Card's attributes through the board array
	 * @param card the card to be removed form the board
	 */
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
	
	/**
	 * adds any card to the player's hand
	 * @param card the card to be added to the hand
	 */
	public void addCardToHand(Card card)
	{
		if (hand.size() < 10)
			hand.add(card);
	}
	
	/**
	 * clears the player's hand. mostly used in testing
	 */
	public void clearHand()
	{
		int h = hand.size() -1;
		for (int i = h ; i >= 0; i--)
		{
			this.hand.remove(i);
		}
	}
	
	/**
	 * Clears the player's board by removing all cards.
	 */
	public void clearBoard()
	{
		int b = board.size() - 1;
		for (int i = b; i >= 0; i--)
		{
			this.board.remove(i);
		}
	}
	
	/**
	 * Method to convert the Player to a String in order to be sent over
	 * the server.
	 * @return a formatted String that can be parsed to reconstruct this
	 *         Player
	 */
	public String toString()
	{
		String string = this.getName() + ";" + this.getCredits() + ";" + this.getWcp() + ";" + this.isFirst +  "\n";
		if (hand.size() != 0)
		{
			for (int i = 0 ; i < hand.size() -1; i++)
				string += hand.get(i).toString() + ";";
			int len = hand.size() - 1;
			string += hand.get(len).toString() + "\n";
		}
		else
			string += "\n";
		if (board.size() != 0)
		{
			for (int i = 0 ; i < board.size() -1; i++)
				string += board.get(i).toString() + ";";
			string += board.get(board.size() - 1).toString() + "\n";
		}
		else
			string += "\n";
		string += deck.getNumDealt();
		return string;
	}
	/**
	 * Method to set whether this player has the first move.
	 * @param b A boolean corresponding to whether or not this player has
	 *          the first move
	 */
	public void setIsFirst(boolean b)
	{
		isFirst = b;
	}
	
	/**
	 * Method to reset cards on the board at the end of a turn so that they
	 * can attack during the following turn.
	 */
	public void resetHasAttacked()
	{
		int len = board.size();
		for (int i = 0 ; i < len ; i ++)
		{
			RegCard card = (RegCard) board.get(i);
			card.setHasAttacked(false);
		}
	}
}