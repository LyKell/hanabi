package fr.umlv.hanabi.pack;
import java.util.Objects;

import fr.umlv.hanabi.card.Card;

public class Discards extends Pack{
	/**
	 * Constructs the discard deck using its super constructor
	 */
	public Discards() {
		super();
	}

	
	/**
	 * Adds a card to the discard deck
	 * @param card the card to add to the discard
	 */
	public void discard(Card card) {
		getCards().add(Objects.requireNonNull(card));
	}
	
	
	/**
	 * Gives the last discarded card (The topmost card of the discard pile)
	 * @return the topmost card of the discard pile or null if the discard pile is empty
	 */
	public Card lastDiscard() {
		var size = sizeOf();
		if (size == 0) {
			return null;
		}
		return getCards().get(size - 1);
	}
}
