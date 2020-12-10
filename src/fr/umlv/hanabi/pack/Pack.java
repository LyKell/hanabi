package fr.umlv.hanabi.pack;
import java.util.ArrayList;

import fr.umlv.hanabi.card.Card;

public class Pack {
	private final ArrayList<Card> cards;

	
	/**
	 * Constructs and initializes the main card deck
	 */
	public Pack() {
		cards = new ArrayList<Card>();
	}

	
	/**
	 * 
	 * @return the entirety of the card of the main deck
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	
	/**
	 * The number of cards in the main deck
	 * @return The number of cards in the main deck
	 */
	public int sizeOf() {
		return cards.size();
	}
}
