package fr.umlv.hanabi.hint;
public enum HintType {
	Color, Value, Invalid;

	
	/**
	 * Converts an input String to its associated HintType
	 * @param string a character representing the hint we want to give
	 * @return the hint type we want to give, as a HintType type
	 */
	public static HintType convertStrToHint(String string) {
		switch(string) {
			case "c":
				return HintType.Color;
			case "v":
				return HintType.Value;
			default:
				return HintType.Invalid;
		}
	}
	
	
	/**
	 * Converts an input int to its associated HintType
	 * @param value an int representing the hint we want to give
	 * @return the hint type we want to give, as a HintType
	 */
	public static HintType convertIntToHint(int value) {
		switch(value) {
		case 0:
			return HintType.Color;
		case 1:
			return HintType.Value;
		default:
			return HintType.Invalid;
		}
	}
}
