package fr.umlv.hanabi.card;

public class Number {
	/**
	 * Checks if the number parametered is legal
	 * @param number the index of a card
	 * @return the index of a card
	 */
	public static int checkNumber(int number) {
		if (number < 1 || number > 5) {	// The card's index is between 1 and 5
			throw new IllegalArgumentException("A number must be in [1, 5]");
		}
		return number;
	}
}
