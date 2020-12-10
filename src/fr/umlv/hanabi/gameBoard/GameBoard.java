package fr.umlv.hanabi.gameBoard;
import java.util.Objects;

import fr.umlv.hanabi.card.Card;
import fr.umlv.hanabi.card.CardColor;
import fr.umlv.hanabi.coin.Coin;
import fr.umlv.hanabi.pack.Deck;
import fr.umlv.hanabi.pack.Discards;
import fr.umlv.hanabi.pack.Pack;

public class GameBoard {
	private final Deck deck;
	private final Discards discard;
	private final Pack board;
	private final Coin red;
	private final Coin blue;
	
	
	/**
	 * Constructs and initializes the gameboard
	 */
	public GameBoard() {
		deck = new Deck();
		discard = new Discards();
		board = new Pack();
		red = new Coin(CardColor.Red, 3);
		blue = new Coin(CardColor.Blue, 8);
		for (var i = 0; i <  CardColor.numberColor(); i++) {
			board.getCards().add(null);
		}
	}
	
	
	/**
	 * Plays a card and verifies if the play is legal
	 * @param card the card to play
	 */
	public void putCard(Card card) {
		Objects.requireNonNull(card, "A card put on the board cannot be null");
		Card onBoard = board.getCards().get(CardColor.fromColor(card.getColor()));
		if ((onBoard == null && card.getNumber() != 1) ||                   // The pile of card.color cards is empty. So we have to play the card #1
				(onBoard != null && (onBoard.getNumber() != card.getNumber() - 1))) {	       	// The pile of card.color cards is not empty
				red.subCoin();
				discard.discard(card);
		}
		else {
			board.getCards().set(CardColor.fromColor(card.getColor()), card);	// We only have to change the shown card (the topmost on the pile)
			if (card.getNumber() == 5) {
				blue.addCoinBlue();
			}
		}
	}
	
	
	/**
	 * Shuffles the deck
	 */
	public void shuffle() {
		deck.shuffle();
	}
	
	
	/**
	 * Give the size of the deck
	 * @return the deck size
	 */
	public int deckSize() {
		return deck.sizeOf();
	}
	
	
	/**
	 * Draws a new card from the deck
	 * @return the card to draw
	 */
	public Card draw() {
		return deck.draw();
	} 
	
	
	/**
	 * Discards a card, and updates the number of blue coins inside and outside of the box
	 * @param card the card to discard
	 * @return false if we can't discard, else true
	 */
	public boolean discard(Card card) {
		Objects.requireNonNull(card, "You cannot discard nothing");
		if (blue.getNumber() == 8) {	// We have all the blue coin
			System.out.println("You have all blue coins, you can't discard a card.");
			return false;
		}
		discard.discard(card);
		blue.addCoinBlue();
		
		return true;
	}
	
	
	/**
	 * The last discarded card, so the topmost card of the discard
	 * @return the last discarded card
	 */
	public Card lastDiscard() {
		return discard.lastDiscard();
	}
	
	
	/**
	 * Updates the number of blue coins after giving a hint
	 * @return false if we can't give a hint, else true
	 */
	public boolean hint() {
		if (blue.getNumber() == 0) {
			System.out.println("You don't have enough blue coin to give a hint.");
			return false;
		}
		blue.subCoin();
		return true;
	}
	
	
	/**
	 * 
	 * @return the red coins
	 */
	public Coin getRed() {
		return red;
	}

	
	/**
	 * 
	 * @return the blue coins
	 */
	public Coin getBlue() {
		return blue;
	}
	

	/**
	 * 
	 * @return the board
	 */
	public Pack getBoard() {
		return board;
	}
}
