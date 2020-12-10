package fr.umlv.hanabi.player;
public enum PlayerAction {
	Play, Discard, Hint, Invalid;

	/**
	 * Converts an input String to its associated Action
	 * @param string a character representing the action we want to do
	 * @return the action we want to do, as an Action type
	 */
	public static PlayerAction convertStrToAction(String string) {
		switch(string) {
			case "p":
				return PlayerAction.Play;
			case "d":
				return PlayerAction.Discard;
			case "h":
				return PlayerAction.Hint;
			default:
				System.out.println("You didn't input a valid action. You must chose between Play, Discard and Hint.");
				return PlayerAction.Invalid;
		}
	}
	
	
	/**
	 * Converts an input int to its associated Action
	 * @param value an int representing the action we want to do
	 * @return the action we want to do, as an Action type
	 */
	public static PlayerAction convertIntToAction(int value) {
		switch (value) {
			case 0:
				return PlayerAction.Play;
			case 1:
				return PlayerAction.Discard;
			case 2:
				return PlayerAction.Hint;
			default:
				return PlayerAction.Invalid;
		}
	}
}
