
package project.deck;

import project.card.Card;

/**
 * Represents a deck of cards, containing 52 standard cards.
 * @author Team Lima
 */
public class DeckofCards implements DeckofCardsInterface{
	
	public static int numCards = 52;
	private Card[] deck;			//deck of cards, top of deck is at index 0
	private int numDealt;			//number of cards dealt
	
	/**
	 * Initializes DeckofCards to contain set number of cards in ascending order.
	 * Order goes Ace of Clubs, Ace of Diamonds, Ace of Hearts, Ace of Spades,
	 * then 2 of Clubs, 2 of Diamonds, 2 of Hearts, 2 of Spades, and so on.
	 * Sets numDealt to 0.
	 * @param size size of deck
	 */
	public DeckofCards(int size)		
	{
		numCards = size;
		//create ordered deck of cards
		deck = new Card[numCards];
		
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
		if (numDealt == 52)
		{
			this.shuffle();
		}
		
		return (deck[numDealt++]);
	}
	
	/**
	 * Returns description of all cards in deck.
	 * @return String description of all cards in deck.
	 */
	public String toString()
	{
		String str = "";
		//incomplete
		return str;
	}	
	
}
