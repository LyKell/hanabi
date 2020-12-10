package fr.umlv.hanabi.card;
import java.awt.Color;

public enum CardColor {
	White, Red, Blue, Yellow, Green;
	

	/**
	 * Converts a color to a number
	 * @param color the color to convert
	 * @return the number representing the color. -1 if it's not a valid color
	 */
	public static int fromColor(CardColor color) {
        switch(color) {
        case White:
            return 0;
        case Red:
            return 1;
        case Blue:
            return 2;
        case Yellow:
            return 3;
        case Green:
            return 4;
        }
        return -1;
    }

	
	/**
	 * Converts a number to a color
	 * @param number the number to convert
	 * @return the color representing the number
	 */
	public static CardColor fromInt(int number) {
        switch(number) {
        case 0:
        	return White;
        case 1:
            return Red;
        case 2:
            return Blue;
        case 3:
            return Yellow;
        case 4:
            return Green;
        }
        throw new IllegalArgumentException();
    }
	
	
	/**
	 * Gives the number of color available
	 * @return the number of color
	 */
	public static int numberColor() {
		var color = CardColor.values();
		return color.length;
	}

	
	/**
	 * Converts an input String to its associated Color
	 * @param string a character representing the color we want to get
	 * @return the color we want to get, as a Color type
	 */
	public static CardColor convertStrToColor(String string) {
		switch(string) {
			case "w":
				return CardColor.White;
			case "r":
				return CardColor.Red;
			case "b":
				return CardColor.Blue;
			case "y":
				return CardColor.Yellow;
			case "g":
				return CardColor.Green;
			default:
				return null;
			}
	}
	
	
	/**
	 * Converts an input int to its associated Color
	 * @param value an int representing the color we want to get
	 * @return the color we want to get, as a Color type
	 */
	public static CardColor convertIntToColor(int value) {
		switch(value) {
			case 0:
				return CardColor.White;
			case 1:
				return CardColor.Red;
			case 2:
				return CardColor.Blue;
			case 3:
				return CardColor.Yellow;
			case 4:
				return CardColor.Green;
			default:
				return null;
			}
	}
	
	
	/**
	 * Gives the length of the name of a color
	 * @return the length of the name of a color
	 */
	public int length() {
		return this.name().length();
	}
	
	
	/**
	 * Converts a CardColor into its Color equivalent
	 * @param color the color to convert
	 * @return the converted Color
	 */
	public static Color convertCardColorToColor(CardColor color) {
		if (color == null) {
			return new Color(110, 110, 110);
		}
		switch (color) {
	        case White:
	            return Color.WHITE;
	        case Red:
	            return Color.RED;
	        case Blue:
	            return Color.BLUE;
	        case Yellow:
	            return Color.YELLOW;
	        case Green:
	            return Color.GREEN;
		}
		return new Color(110, 110, 110);
	}
}
