
package project.deck;

import project.card.Card;

/**
 * Represents a deck of cards, containing 52 standard cards.
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
		int j;
		for (int i = 0; i < numCards; i++)
		{
			j = (int) (len * Math.random());
			deck[i] = baseDeck[j];
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
	 * Returns description of all cards in deck.
	 * @return String description of all cards in deck.
	 */
	public String toString()
	{
		String deckstring = "";
		int index = 0;
		for ( int i = 0; i < (cardsLeft()); i++ ) {
			
			deckstring += (deck[index++] + "\n");
		}
		return ( deckstring );
	}
	
	public Card[] getDeck()
	{
		return deck;
	}
	
	public boolean isEmpty()
	{
		if (this.cardsLeft() <= 0)
		{
			return true;
		}
		return false;
	}
			
}
