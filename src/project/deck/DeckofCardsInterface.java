
package project.deck;

import project.card.Card;

/**
 * Specifies the properties of a deck of playing cards.
 * @author Team Lima
 */
public interface DeckofCardsInterface {
	
	/**
	 * Shuffles the deck of cards and resets deal to
	 * beginning of deck.
	 */
	void shuffle();
	
	/**
	 * Returns number of cards still in deck.
	 * @return Integer representing number of cards left in deck.
	 */
	int cardsLeft();
	
	/**
	 * Returns next card in deck. Shuffles deck if no cards are left.
	 * @return Card to be dealt next
	 */
	Card dealCard();
	
	/**
	 * Returns description of cards in deck.
	 * @return String description of cards in deck.
	 */
	String toString();
	//returns a string representing the entire deck of cards
	
}