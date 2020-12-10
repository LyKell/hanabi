package fr.umlv.hanabi.pack;
import java.util.Collections;

import fr.umlv.hanabi.card.Card;
import fr.umlv.hanabi.card.CardColor;

public class Deck extends Pack{
	/**
	 * Constructs and initializes the card deck (The entirety of the cards)
	 */
	public Deck() {
		super();
		for (CardColor c : CardColor.values()) {
			var card1 = new Card(c, 1);
			var card2 = new Card(c, 2);
			var card3 = new Card(c, 3);
			var card4 = new Card(c, 4);
			var card5 = new Card(c, 5);
			
			for (var i = 0; i < 3; i++) {
				getCards().add(card1);
			}
			for (int i = 0; i < 2; i++) {
				getCards().add(card2);
				getCards().add(card3);
				getCards().add(card4);
			}
			getCards().add(card5);
		}
	}
	
	
	/**
	 * Shuffles the entirety of the cards
	 */
	public void shuffle() {
		Collections.shuffle(getCards());
	}
	
	
	/**
	 * Draws, so removes a card from the deck
	 * @return the drawn card or null if the deck is empty
	 */
	public Card draw() {
		var size = sizeOf();
		if (size == 0) {
			return null;
		}
		return getCards().remove(size - 1);
	}
}
