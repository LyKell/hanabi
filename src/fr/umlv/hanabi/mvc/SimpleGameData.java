package fr.umlv.hanabi.mvc;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.hanabi.card.CardColor;
import fr.umlv.hanabi.gameBoard.GameBoard;
import fr.umlv.hanabi.graphic.GraphicsData;
import fr.umlv.hanabi.hint.HintType;
import fr.umlv.hanabi.player.Player;
import fr.umlv.hanabi.player.PlayerAction;
import fr.umlv.zen5.ApplicationContext;

public class SimpleGameData {
	private final ArrayList<Player> players;
	private final GameBoard gameBoard;
	private int turnLeft = -1;

	
	/**
	 * Constructs and initializes a game data with the specified number of players
	 * @param nbPlayer the number of players in the game
	 */
	public SimpleGameData(int nbPlayer) {
		Player.testNumberPlayer(nbPlayer);

		players = new ArrayList<Player>();
		gameBoard = new GameBoard();
		gameBoard.shuffle();
		
		int nbCards;
		if (nbPlayer <= 3) {
			nbCards = 5;
		} else {
			nbCards = 4;
		}
		
		for (int i = 0; i < nbPlayer; i ++) {
			players.add(new Player(gameBoard, nbCards));
		}
	}
	

	/**
	 * Decides which action the player wants to do
	 * @param playerNumber the number of players in the game
	 * @param playerIndex the number of the player who is currently playing
	 * @param action the character that defines an action that the player has input
	 * @param input the scanner which is parse
	 * @return true if the action is legal, false if it is not
	 */
	public boolean playerAction(int playerNumber, int playerIndex, String action, Scanner input) {
		Player.testNumberPlayer(playerNumber);
		Player.testIndexPlayer(playerIndex, playerNumber);
		Objects.requireNonNull(action, "action can't be null");
		Objects.requireNonNull(input, "A scanner cannot be null");
		
		var newAction = PlayerAction.convertStrToAction(action);
		if (newAction.equals(PlayerAction.Hint)) {
			if (gameBoard.hint()) {	// Checks if we have enough blue coins to give a hint
				return choosePlayer(playerNumber, playerIndex, input);
			}
		} else if (!newAction.equals(PlayerAction.Invalid)){
			return playCard(playerIndex, newAction, input);
		}
		return false;
	}
	
	
	/**
	 * Decides which action the player want to do between Playing and Discarding a card
	 * @param playerIndex the number of the player who is currently playing
	 * @param newAction the action the player wants to do
	 * @param input the scanner which is parse
	 * @return true if the action is legal, false if we can't discard
	 */
	boolean playCard(int playerIndex, PlayerAction newAction, Scanner input) {
		int index;
		var player = players.get(playerIndex);
		var maxIndex =  player.sizeOfHand();
		do {
			index = SimpleGameController.askForInt(input,"\nChoose the index of the card you want to play between [0," + 
																							      (maxIndex - 1) + "]");
		} while (0 > index || maxIndex <= index);
		
		return actionCard(player, index, newAction);
	}
	
	
	/**
	 * Plays or Discards the player's card and draws a new one
	 * @param player the player who is currently playing
	 * @param index the index of the card the player wants to play
	 * @param newAction the action performed by the player
	 * @return 
	 */
	boolean actionCard(Player player, int index, PlayerAction newAction) {
		var card = player.play(index);
		if (newAction.equals(PlayerAction.Play)) {
			gameBoard.putCard(card);
		} else if (newAction.equals(PlayerAction.Discard)) {
			if (!gameBoard.discard(card)) {
				player.draw(card, index);
				return false;
			}
		}
		player.draw(gameBoard.draw(), index);
		if (gameBoard.deckSize() == 0 && turnLeft == -1) {	// The deck is empty, so each player have a final move to make
			turnLeft = players.size() + 1;
		}
		return true;
	}
	
	
	/**
	 * Selects the player we want to give a hint to
	 * @param playerNumber the number of players in the game
	 * @param playerIndex the number of the player who is currently playing
	 * @param input the scanner which is parse
	 * @return true if we can give a hint, false if we want to give a hint to ourselves
	 */
	boolean choosePlayer(int playerNumber, int playerIndex, Scanner input) {
		var selection = SimpleGameController.askForInt(input,"\nSelect the player you want to give a hint to");
		if (selection == playerIndex + 1 || selection <= 0 || selection > playerNumber) {	// playerIndex starts at 0
			System.out.println("You can't give a hint to an invalid person");
			return false;
		}
		
		return chooseHintType(selection - 1, input);
	}
	
	
	/**
	 * Selects the hint type we want to give
	 * @param playerIndex the number of the player we want to give a hint to
	 * @param input the scanner which is parsed
	 * @return true
	 */
	boolean chooseHintType(int playerIndex, Scanner input) {
		var newHint = HintType.convertStrToHint(SimpleGameController.askForString(input,
				"\nWhat kind of hint do you want to give ? Color or Value (c / v)"));
		
		if (newHint.equals(HintType.Color)) {
			chooseColor(playerIndex, input);
		} else if (newHint.equals(HintType.Value)) {
			chooseValue(playerIndex, input);
		}
		else {
			return chooseHintType(playerIndex, input);
		}
		return true;
	}
	
	
	/**
	 * Selects a color for the hint
	 * @param playerIndex the number of the player we want to give a hint to
	 * @param input the scanner which is parse 
	 */
	void chooseColor(int playerIndex, Scanner input) {
		CardColor color;
		
		do {
		color = CardColor.convertStrToColor(SimpleGameController.askForString(input,
				                         "\nChoose the color between White, Red, Blue, Yellow and Green (w / r / b / y / g)"));
		} while (color == null);
		players.get(playerIndex).hint(color);
	}
	
	
	/**
	 * Selects a value for the hint
	 * @param playerIndex the number of the player we want to give a hint to
	 * @param input the scanner which is parse 
	 */
	void chooseValue(int playerIndex, Scanner input) {
		int value;
		do {
		value = SimpleGameController.askForInt(input, "\nChoose the value in [1, 5]");
		} while (value < 1 || value > 6);
		
		players.get(playerIndex).hint(value);
	}
	

	/**
	 * Gives the list of all players in the game
	 * @return the list of players
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	
	/**
	 * Gives the current state of the gameboard
	 * @return the gameboard
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}
	

	/**
	 * Checks if the game is in its last turn or not
	 * @return -1 if the game is not over yet. The score of the players if the game is over
	 */
	private int calcTurnLeft() {
		if (gameBoard.deckSize() != 0) {
			return -1;
		}
		
		turnLeft --;// Each time a player turn is over
		if (turnLeft != 0) {
			return -1;
		}
		return score();	// The game is now over
	}
	

	/**
	 * Grants the players a perfect score or not
	 * @return the perfect score or -1
	 */
	private int fullHanabi() {
		if (score() == CardColor.numberColor() * 5) {
			return CardColor.numberColor() * 5;
		}
		return -1;
	}
	
	
	/**
	 * Calculates the score for each card pile
	 * @return the score for each card pile
	 */
	private int score() {
		var count = 0;
		for (var card : gameBoard.getBoard().getCards()) {
			if (card != null) {
				count += card.getNumber();
			}
		}
		return count;
	}
	

	/**
	 * Ends the game if they are no red coins left
	 * @return the score of the players if they won. 0 if they don't have red coins left. -1 if the game is not over yet
	 */
	public int endGame() {
		int score;
		if (gameBoard.getRed().getNumber() == 0) {
			return 0;
		}
		score = calcTurnLeft();
		if (score >= 0)
			return score;
		return fullHanabi();	// The game can be over when all 5 fireworks are complete
	}
	
	
	/**
	 * Recovers the player from the mouse click location
	 * @param location the position of the mouse click
	 * @param numberOfButton the number of button in this state
	 * @param playerIndex the index of the currently playing player. Used only if state is 1
	 * @param state an int that defines the current state of the game
	 * @return the index of the option associated to the clicked button (Player or Back), or -1 if no button were pressed
	 */
	int recoverButtonsLocation(Point2D.Float location, int numberOfButton, int playerIndex, int state) {
		var x = location.x;
		var y = location.y;
		var posX = 0;
		var posY = 0;
		
		return recoverButtonsLocationOnButton(x, y, numberOfButton, playerIndex, posY, posX, state);	// Intermediary method in order to factorize the code
	}
	
	
	/**
	 * Recovers the player from the mouse click location. This method is an intermediary method for recoverPlayerLocation in order to reduce its size
	 * @param x the X coordinate of the mouse click
	 * @param y the Y coordinate of the mouse click
	 * @param numberOfButtons the number of button in this state
	 * @param playerIndex the index of the currently playing player. Used only if state is 1
	 * @param currentButtonIndex the button we are checking if the mouse was clicked on
	 * @param posY the Y coordinate of the first button
	 * @param posX the X coordinate of the first button
	 * @param state an int that defines the current state of the game
	 * @return the index of the option associated to the clicked button (Player or Back), or -1 if no button were pressed
	 */
	private int recoverButtonsLocationOnButton(float x, float y, int numberOfButtons, int playerIndex, int posY, int posX, int state) {
		var i = 0;
		var j = 0;	// An index to get the button position on Y axis
		var k = 0;	// An index to get the button position on X axis
		var currentButtonIndex = 0;
		
		for (currentButtonIndex = 0; currentButtonIndex < numberOfButtons; currentButtonIndex++) {			
			if (k == 3) {	// New line of button
				j++;
				k = 0;
				i = 0;
			}
			
			posY = (int) (GraphicsData.buttonPosY + j * 100 * SimpleGameView.RATIO_HEIGHT);
			if (y >= posY && y <= posY + GraphicsData.buttonHeight) {	// Checking for the Y position of the mouse click
				posX = GraphicsData.buttonPosX + i * (GraphicsData.buttonWidth + GraphicsData.buttonPosX);
				if (x >= posX && x <= posX + GraphicsData.buttonWidth) {	// Checking for the X position of the mouse click
					return currentButtonIndex + 1;
				}
			}
			
			if (state == 1) {	// Players selection is the current State
				if (!(currentButtonIndex == playerIndex)) {	// If we are not checking on the player who is currently playing, we increment k
					k++;
				} else if (currentButtonIndex == 0) {
					k++;
				} else if (playerIndex != 0) {
					k++;
				}
			} else {	// Color or Number selection is the current State
				k++;
			}
			i++;
		}
		return -1;
	}

	
	/**
	 * Recovers the card from the mouse click location
	 * @param location the location of the mouse click
	 * @param cardsInHand the number of cards in the player's hand
	 * @param player the currently playing player
	 * @return
	 */
	private int recoverCardsLocation(Point2D.Float location, int cardsInHand, Player player) {
		var x = location.x;
		var y = location.y;
		var posX = (int) ((SimpleGameView.getWidth() - 215 * SimpleGameView.RATIO_WIDTH * cardsInHand - (cardsInHand - 1)) / 2);
		var posY = GraphicsData.currentCardPosY;
		var cardX = 0;
		var button = 0;
		
		for (var i = 0; i < player.getHand().sizeOf(); i++) {
			cardX = (int) (posX + (GraphicsData.currentCardWidth + 4 * SimpleGameView.RATIO_WIDTH) * i);
			if (y >= posY && y <= posY + GraphicsData.currentCardHeight) {
				if (x >= cardX && x <= cardX + GraphicsData.currentCardWidth) {
					return i;	// We've clicked on a card
				}
			}
		}
		
		/* Here, we are not clicking on a card */
		button = recoverButtonsLocation(location, 1, 0, 0);
		if (button == -1) {	// No button clicked
			return button;
		} else {	// Back button cliked
			return button + player.getHand().sizeOf();	// In order to avoid confusion with a card index, we add the number of card in the hand of the player to the button
		}
	}
	
	
	/**
	 * Recovers the number of players for the game 
	 * @param location the location of the mouse click
	 * @param posX the X coordinate of the first button
	 * @param width the width of the buttons
	 * @param height the height of the buttons
	 * @return the number of players
	 */
	private static int recoverPlayerNumberLocation(Point2D.Float location, int posX, int width, int height) {
		var i = 0;
		var x = location.x;
		var y = location.y;
		var posY = 0;
		for (i = 0; i < 4; i++) {
			posY = (int) (GraphicsData.menuPosY + i * (GraphicsData.menuButtonHeight + 30 * SimpleGameView.RATIO_HEIGHT) + 96 * SimpleGameView.RATIO_HEIGHT);
			if (posX < x && x < posX + width) {
				if (posY < y && y < posY + height) {
					return i + 2;
				}
			}
		}
		return -1;
	}
		

	/**
	 * Decides which action the player wants to do
	 * @param playerNumber the number of player in the game
	 * @param playerIndex the index of the player who is currently playing
	 * @param action the character that defines an action that the player has input
	 * @param context the {@code ApplicationContext} of the game
	 * @return true if the action is legal, false if it is not
	 */
	public boolean playerAction(int playerNumber, int playerIndex, PlayerAction action, ApplicationContext context) {
		Player.testNumberPlayer(playerNumber);
		Player.testIndexPlayer(playerIndex, playerNumber);
		
		if (action.equals(PlayerAction.Hint)) {
			if (gameBoard.getBlue().getNumber() != 0) {
				return choosePlayer(playerNumber, playerIndex, context);
			}
		} else if (!action.equals(PlayerAction.Invalid)) {
			if (playCard(playerIndex, action, context)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Decides which action the player want to do between Playing and Discarding a card
	 * @param playerIndex the number of the player who is currently playing
	 * @param newAction the action the player wants to do
	 * @param context the {@code ApplicationContext} of the game
	 * @return true if the action is legal, false if we can't discard
	 */	
	boolean playCard(int playerIndex, PlayerAction newAction, ApplicationContext context) {
		var player = players.get(playerIndex);
		var cardsInHand = player.sizeOfHand();
		var selection = -1;
		Point2D.Float location;
		LinkedHashMap<String, Color> map = new LinkedHashMap<String, Color>();
		map.put("Back", new Color (110, 110, 110));
		context.renderFrame(graphics -> SimpleGameView.viewMenu(graphics, map));
		
		do {
			location = SimpleGameController.retrieveMouseClick(context);
			selection = recoverCardsLocation(location, cardsInHand, player);
			if (selection == player.getHand().sizeOf() + 1) {
				return false;
			}
		} while (selection == -1);
		
		return actionCard(player, selection, newAction);
	}
	
	
	/**
	 * Adds the buttons to an ArrayList and selects the player we want to give a hint to
	 * @param playerNumber the number of player in the game
	 * @param playerIndex the number of the player who is currently playing
	 * @param context the {@code ApplicationContext} of the game
	 * @return true if we can give a hint, false if we hit the Back button
	 */
	boolean choosePlayer(int playerNumber, int playerIndex, ApplicationContext context) {
		LinkedHashMap<String, Color> map = new LinkedHashMap<String, Color>();
		for (var i = 0; i < playerNumber; i++) {
			if (!(i == playerIndex)) {
				map.put("Joueur " + (i + 1), new Color(205, 205, 205));
			}
		}
		map.put("Back", new Color(110, 110, 110));
		context.renderFrame(graphics -> SimpleGameView.viewMenu(graphics, map));
		return choosePlayerLoop(map.size(), playerIndex, context, map);
	}
	
	
	/**
	 * Selects the player we want to give a hint to. This method is an intermediary method for choosePlayer in order to reduce its size
	 * @param numberOfButtons the number of player in the game
	 * @param playerIndex the number of the player who is currently playing
	 * @param context the {@code ApplicationContext} of the game
	 * @param map a LinkedHashMap to store the buttons' label and color
	 * @return true if we can give a hint, false if we hit the Back button
	 */
	boolean choosePlayerLoop(int numberOfButtons, int playerIndex, ApplicationContext context, LinkedHashMap<String, Color> map) {
		Objects.requireNonNull(map);
		Point2D.Float location;
		var selection = -1;
		
		do {
			location = SimpleGameController.retrieveMouseClick(context);
			selection = recoverButtonsLocation(location, numberOfButtons, playerIndex, 1);
			if (selection <= playerIndex && selection != -1) {
				selection--;
			}
			if (selection == numberOfButtons) {	// Back button
				return false;
			}
			
			if (selection != -1 && chooseHintType(selection, context)) {
				return true;
			} else {
				context.renderFrame(graphics -> SimpleGameView.viewMenu(graphics, map));
			}
		} while (true);
	}
	

	/**
	 * Selects the hint type we want to give
	 * @param playerIndex the number of the player we want to give a hint to
	 * @param context the {@code ApplicationContext} of the game
	 * @return true if we give a hint, false if we hit the Back button
	 */
	boolean chooseHintType(int playerIndex, ApplicationContext context) {
		Point2D.Float location;
		var selection = -1;
		var hint = HintType.Invalid;
		
		LinkedHashMap<String, Color> map = new LinkedHashMap<String, Color>();
		map.put("Color", new Color(205, 205, 205));
		map.put("Number", new Color(205, 205, 205));
		map.put("Back", new Color(110, 110, 110));
		context.renderFrame(graphics -> SimpleGameView.viewMenu(graphics, map));
		
		do {
			location = SimpleGameController.retrieveMouseClick(context);
			selection = recoverButtonsLocation(location, map.size(), 0, 0) - 1;
			if (selection == map.size() - 1) {
				return false;
			}
			
			hint = HintType.convertIntToHint(selection);
			if (chooseHintTypeLoop(hint, playerIndex, context, map)) {
				gameBoard.hint();
				return true;
			}
		} while (true);
	}
	
	
	/**
	 * Selects the hint type we want to give. This method is an intermediary method for chooseHintType in order to reduce its size
	 * @param hint the hint type we want to give
	 * @param playerIndex the number of the player we want to give a hint to
	 * @param context the {@code ApplicationContext} of the game
	 * @param map a LinkedHashMap to store the buttons' label and color
	 * @return true if we give a hint, false if we hit the Back button
	 */
	boolean chooseHintTypeLoop(HintType hint, int playerIndex, ApplicationContext context, LinkedHashMap<String, Color> map) {
		if (hint.equals(HintType.Color)) {
			 if (chooseColor(playerIndex, context)) {
				 return true;
			 }
		} else if (hint.equals(HintType.Value)) {
			 if (chooseValue(playerIndex, context)) {
				 return true;
			 }
		}
		context.renderFrame(graphics -> SimpleGameView.viewMenu(graphics, map));
		return false;
	}
	
	
	/**
	 * Selects a color for the hint
	 * @param playerIndex the number of the player we want to give a hint to
	 * @param context the {@code ApplicationContext} of the game
	 */
	boolean chooseColor(int playerIndex, ApplicationContext context) {
		Point2D.Float location;
		var selection = 0;
		CardColor cardColor;
		
		LinkedHashMap<String, Color> map = new LinkedHashMap<String, Color>();
		for (CardColor color : CardColor.values()) {
			map.put(color.toString(), CardColor.convertCardColorToColor(color));
		}
		map.put("Back", new Color(110, 110, 110));
		
		context.renderFrame(graphics -> SimpleGameView.viewMenu(graphics, map));
		
		do {
			location = SimpleGameController.retrieveMouseClick(context);
			selection = recoverButtonsLocation(location, map.size(), 0, 0);
			cardColor = CardColor.convertIntToColor(selection - 1);
			if (selection == map.size()) {
				return false;
			}
		} while (cardColor == null);
		players.get(playerIndex).hint(cardColor);
		return true;
	}
	
	
	/**
	 * Selects a value for the hint
	 * @param playerIndex the number of the player we want to give a hint to
	 * @param context the {@code ApplicationContext} of the game
	 * @return true if we gave a hint, false if we hit the Back button
	 */	
	boolean chooseValue(int playerIndex, ApplicationContext context) {
		Point2D.Float location;
		var selection = 0;
		
		LinkedHashMap<String, Color> map = new LinkedHashMap<String, Color>();
		for (int i = 1; i <= 5; i++) {
			map.put(Integer.toString(i), new Color(205, 205, 205));
		}
		map.put("Back", new Color(110, 110, 110));
		
		context.renderFrame(graphics -> SimpleGameView.viewMenu(graphics, map));
		
		do {
			location = SimpleGameController.retrieveMouseClick(context);
			selection = recoverButtonsLocation(location, map.size(), 0, 0);
			if (selection == map.size()) {
				return false;
			}
		} while (selection < 0 || selection > 5);
		
		players.get(playerIndex).hint(selection);
		return true;
	}
	
	
	/**
	 * Verifies if the number of players for the game is correct
	 * @param context the {@code ApplicationContext} of the game
	 * @param x the X coordinate of the first button
	 * @param width the width of the button
	 * @param height the height of the button
	 * @return the number of players
	 */
	static int retrieveNumberOfPlayers(ApplicationContext context, int x, int width, int height) {
		Point2D.Float location;
		var playerNumber = 0;
		do {
			location = SimpleGameController.retrieveMouseClick(context);
			playerNumber = recoverPlayerNumberLocation(location, x, width, height);
		} while (playerNumber < 2 || playerNumber > 5);
		
		return playerNumber;
	}
}
