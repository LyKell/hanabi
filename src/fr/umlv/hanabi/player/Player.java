package fr.umlv.hanabi.player;
import java.util.ArrayList;
import java.util.Objects;

import fr.umlv.hanabi.card.Card;
import fr.umlv.hanabi.card.CardColor;
import fr.umlv.hanabi.gameBoard.GameBoard;
import fr.umlv.hanabi.hint.Hint;
import fr.umlv.hanabi.pack.Pack;

public class Player{
	private final Pack hand;
	private final ArrayList<Hint> hints;	
	
	
	/**
	 * Constructs and initializes a player with a gameboard and a number of cards
	 * @param board the gameboard on which we put all the cards and coins
	 * @param nbCards the number of cards per player
	 */
	public Player(GameBoard board, int nbCards) {
		Objects.requireNonNull(board, "board cannot be null");
		if (nbCards != 4  && nbCards != 5) {
			throw new IllegalArgumentException("The number of cards must be between 4 and 5.");
		}
		this.hand = new Pack();
		this.hints = new ArrayList<Hint>();
		for (var i = 0; i < nbCards; i ++) {
			this.hand.getCards().add(board.draw());
			this.hints.add(new Hint());
		}
	}

	
	/**
	 * Draws and adds a card to the player's hand
	 * @param card the drawn card
	 * @param index the index of the former card in order to put the new card at the same place
	 */
	public void draw(Card card, int index) {
		if (card != null) {
			hand.getCards().add(index, card);
		}
		else {
			hints.remove(index);
		}
	}
	
	
	/**
	 * Plays and removes a card of the player's hand
	 * @param index the index of the card to play and to remove of the player's hand
	 * @return The card to play and to remove of the player's hand
	 */
	public Card play(int index) {
		if (index < 0 || index >= sizeOfHand()) {
			throw new IllegalArgumentException("Index have to be valid");
		}
		hints.get(index).resetHint();
		return hand.getCards().remove(index);
	}
	
	
	/**
	 * Gives a color hint for the player
	 * @param color the color of the hint
	 */
	public void hint(CardColor color) {
		var i = 0;
		for (var card : hand.getCards()) {
			if (card.getColor().equals(color)) {
				hints.get(i).setColor(color);
			}
			i++;
		}
	}

	
	/**
	 * Gives a value hint for the player
	 * @param value the value of the hint
	 */
	public void hint(int value) {
		var i = 0;
		for (var card : hand.getCards()) {
			if (card.getNumber() == value) {
				hints.get(i).setNumber(value);
			}
			i++;
		}
	}
	
	
	/**
	 * The number of cards in the player's hand
	 * @return the number of cards in the player's hand
	 */
	public int sizeOfHand() {
		return hand.sizeOf();
	}
	
	
	/**
	 * 
	 * @return the hints
	 */
	public ArrayList<Hint> getHints() {
		return hints;
	}

	
	/**
	 * 
	 * @return the hand
	 */
	public Pack getHand() {
		return hand;
	}

	
	/**
	 * Tests if the number of player entered is legal
	 * @param nbPlayer the input number of players
	 */
	public static void testNumberPlayer(int nbPlayer) {
		if (nbPlayer < 2 || nbPlayer > 5) {
			throw new IllegalArgumentException("The number of players must be between 2 and 5.");
		}
		
	}

	
	/**
	 * Tests if the index of the player entered is legal
	 * @param playerIndex the input index of player
	 * @param playerNumber the number of players in the game
	 */
	public static void testIndexPlayer(int playerIndex, int playerNumber) {
		if (0 > playerIndex || playerNumber < playerIndex) {
			throw new IllegalArgumentException("The player's number must be between 0 and " + Integer.toString(playerNumber));
		}
	}
}
