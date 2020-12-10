package fr.umlv.hanabi.hint;
import java.util.Objects;

import fr.umlv.hanabi.card.CardColor;
import fr.umlv.hanabi.card.Number;

public class Hint {
	private CardColor color;
	private Integer number;
	
	
	/**
	 * Constructs and initializes a hint with a color and a number
	 * @param color the color of a card
	 * @param number the number of a card
	 */
	private Hint(CardColor color, Integer number) {
		if (this.color == null) {
			this.color = color;
		}
		
		if (this.number == null) {
			this.number = number;
		}
	}
	
	
	/**
	 * Constructs a hint with null values
	 */
	public Hint() {
		this(null, null);
	}
	
	
	/**
	 * 
	 * @param color the new color
	 */
	public void setColor(CardColor color) {
		Objects.requireNonNull(color, "Color cannot be null");
		this.color = color;
	}
	
	
	/**
	 * 
	 * @param number the new number
	 */
	public void setNumber(Integer number) {
		Objects.requireNonNull(number, "number cannot be null");
		Number.checkNumber(number);
		this.number = number;
	}
	
	
	/**
	 * 
	 * @return the current color of the hint
	 */
	public CardColor getColor() {
		return color;
	}
	
	
	/**
	 * 
	 * @return the current number of the hint
	 */
	public Integer getNumber() {
		return number;
	}
	
	
	/**
	 * Resets a hint when it is of no use anymore
	 */
	public void resetHint() {
		this.color = null;
		this.number = null;
	}

}
