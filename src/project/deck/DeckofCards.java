
package project.deck;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import project.card.Card;

/**
 * Represents a deck of cards.
 * @author Team Lima
 */
public class DeckofCards implements DeckofCardsInterface{
	
	public static int numCards;
	protected Card[] deck;			//deck of cards, top of deck is at index 0
	protected int numDealt;			//number of cards dealt
	
	/**
	 * Creates new DeckofCards from a standard base deck.
	 * Builds new deck by randomly choosing cards from the base deck.
	 * Based deck created in Board.java method initCards() method.
	 * Precondition: size is smaller than baseDeck.length
	 * @param size size of deck
	 * @param baseDeck deck from which this deck is build from
	 */
	public DeckofCards(int size, Card[] baseDeck)
	{
		numCards = size;
		//create ordered deck of cards
		deck = new Card[numCards];
		int len = baseDeck.length;
		
		Random rng = new Random();
		
		Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < numCards)
		{
			Integer next = rng.nextInt(len);
			generated.add(next);
		}
		int j;
		Iterator iterator = generated.iterator();
		int i = 0;
		while(iterator.hasNext())
		{
			j = (Integer) iterator.next();
			deck[i] = baseDeck[j];
			i++;
		}
	}
	
	/**
	 * Shuffles deck using 500 exchanges of random cards.
	 * Resets number dealt to 0.
	 */
	@Override
	public void shuffle()
	{
		//reset numDealt
		numDealt = 0;
		
		int card1, card2;
		for (int i = 0; i < 500; i++)
		{
			//generate random cards to be swamped
			card1 = (int) (numCards * Math.random());
			card2 = (int) (numCards * Math.random());
			//swamp cards
			Card tmp = deck[card1];
			deck[card1] = deck[card2];
			deck[card2] = tmp;
		}
	}
	
	/**
	 * Returns number of cards left in the deck.
	 * @return Integer representing number of cards left in deck.
	 */
	@Override
	public int cardsLeft()
	{
		return numCards - numDealt;
	}
	
	/**
	 * Deals a card from the deck, shuffles deck if no more cards left to be dealt.
	 * @return Card representing the top most card in the deck.
	 */
	public Card dealCard()
	{
		return (deck[numDealt++]);
	}
	
	/**
	 * Deck observer
	 * @return an array of cards that constitutes the deck
	 */
	public Card[] getDeck()
	{
		return deck;
	}
	
	/**
	 * Gets number of cards already dealt from the deck.
	 * @return an integer that is the number of card's already dealt from the deck
	 */
	public int getNumDealt()
	{
		return numDealt;
	}
	
	/**
	 * Transformer that sets numDealt to a specified value
	 * @param nd the number that numDealt will be set to
	 */
	public void setNumDealt(int nd)
	{
		this.numDealt = nd;
	}
	
	/**
	 * Method to determine whether a DeckofCards is empty or not
	 * @return true if deck is empty
	 */
	public boolean isEmpty()
	{
		if (this.cardsLeft() <= 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * Utilizes the Card's method toString to print out the array of cards that constitutes the deck
	 * @return a nicely formatted String representing the DeckofCards in sequential order
	 */
	public String toString()
	{
		String string = "";
		for (int i = 0 ; i < deck.length-1; i++)
			string += deck[i].toString() + ";";
		string += deck[deck.length -1].toString() + "\n";
		return string;
	}
	
}