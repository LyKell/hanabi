package fr.umlv.hanabi.card;
import java.util.Objects;

public class Card {
	private final CardColor color;
	private final int number;
	
	
	/**
	 * Constructs and initializes a card with its color and its number
	 * @param color the color of the card
	 * @param number the number of the card
	 */
	public Card(CardColor color, int number) {
		this.color = Objects.requireNonNull(color, "A color cannot be null");
		this.number = Number.checkNumber(number);
	}
	

	/**
	 * 
	 * @return the color of the card
	 */
	public CardColor getColor() {
		return color;
	}

	
	/**
	 * 
	 * @return the number of the card
	 */
	public int getNumber() {
		return number;
	}
}
