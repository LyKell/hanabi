package fr.umlv.hanabi.coin;
import fr.umlv.hanabi.card.CardColor;

public class Coin {
	private final CardColor color;
	private int number;

	
	/**
	 * Constructs and initializes coins with the number of said coins
	 * @param color the color of the coin
	 * @param number the total number of a certain kind of coins
	 */
	public Coin(CardColor color, int number) {
		if (color != CardColor.Red && color != CardColor.Blue) {
			throw new IllegalArgumentException();
		}
		this.color = color;
		this.number = number;
	}
	
	
	/**
	 * Substracts a coin
	 */
	public void subCoin() {
		if (number == 0) {
			throw new IllegalStateException();
		}
		number --;
	}
	
	
	/**
	 * Adds a blue coin if it's possible
	 */
	public void addCoinBlue() {
		if (number < 8) {
			addCoin();
		}
	}
	
	
	/**
	 * Adds a coin
	 */
	private void addCoin() {
		number ++;
	}
	
	/**
	 * 
	 * @return the color of the coin
	 */
	public CardColor getColor() {
		return color;
	}
	
	/**
	 * 
	 * @return the current number of coins
	 */
	public int getNumber() {
		return number;
	}
}
