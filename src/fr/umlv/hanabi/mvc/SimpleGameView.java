package fr.umlv.hanabi.mvc;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Objects;

import fr.umlv.hanabi.card.Card;
import fr.umlv.hanabi.card.CardColor;
import fr.umlv.hanabi.coin.Coin;
import fr.umlv.hanabi.gameBoard.GameBoard;
import fr.umlv.hanabi.hint.Hint;
import fr.umlv.hanabi.player.Player;
import fr.umlv.hanabi.graphic.GraphicsData;
import fr.umlv.zen5.ApplicationContext;

public class SimpleGameView {
	private static int width;
	private static int height;
	public static float RATIO_WIDTH;
	public static float RATIO_HEIGHT;

	
	/**
	 * Constructs and initializes a SimpleGameView with the window width and height
	 * @param w the width of the window
	 * @param h the height of the window
	 */
	public SimpleGameView(int w, int h) {
		width = w;
		height = h;
		RATIO_WIDTH = ((float) w / GraphicsData.DEFAULT_WIDTH);
		RATIO_HEIGHT = ((float) h / GraphicsData.DEFAULT_HEIGHT);
	}
	
	
	/**
	 * Prints the current state of the playing board
	 * @param board the current board
	 */
	private static void viewBoard(GameBoard board) {
		var pack = board.getBoard().getCards();
		var str = new StringBuilder();
		str.append("Board :\n");
		for (var number = 1; number <= 5; number++) {
			for (var color = 0; color <  CardColor.numberColor(); color++) {
				if (pack.get(color) != null && pack.get(color).getNumber() >= number) {
					str.append(number).append(" ").append(pack.get(color).getColor()).append(" | ");
				}
				else {
					str.append("  ");
					for (int blank = 0; blank < CardColor.fromInt(color).length(); blank++) {	// Necessary size to print the color name
						str.append(" ");
					}
					str.append(" | ");
				}
			}
			str.append("\n");
		}
		System.out.println(str.toString());
	}

	
	/**
	 * Prints the coins left
	 * @param coin the coins we want to print
	 */
	private static void viewCoin(Coin coin) {
		System.out.println(coin.getColor() + " Coin : " + coin.getNumber());
	}

	
	/**
	 * Prints the discard pile (The last discarded card, so the topmost card on the discard pile)
	 * @param board the current board
	 */
	private static void viewLastDiscard(GameBoard board) {
		var card = board.lastDiscard();
		var str = new StringBuilder();
		str.append("\nDiscard : ");
		if (card != null) {	// If the discard pile is not empty
			str.append(card.getNumber()).append(" ").append(card.getColor());
		}
		System.out.println(str.toString());
	}


	
	/**
	 * Prints the entirety of the gameboard
	 * @param data the data of the current game
	 */
	private static void viewGameboard(SimpleGameData data) {
		var board = data.getGameBoard();
		viewLastDiscard(board);
		viewCoin(board.getBlue());
		viewCoin(board.getRed());
		viewBoard(board);
		viewLastTurnMessage(board);
	}
	
	/**
	 * Prints a message if it's the last turn of the player
	 * @param data the data of the current game
	 */
	private static void viewLastTurnMessage(GameBoard board) {
		if (board.deckSize() == 0) {
			System.out.println("It's your last turn\n");
		}
	}


	/**
	 * Gives the String object if it is not null
	 * @param object the object we want to print
	 * @return the object in String or " ? " if the object is null (unknown to the player)
	 */
	private static String nullOrPrintable(Object object) {
		if (object == null) {
			return " ? ";
		}
		else {
			return object.toString();
		}
	}
	
	/**
	 * Prints what the current player knows (His hints and the other players cards)
	 * @param playerNumber the number of players in the game
	 * @param data the data of the current game
	 * @param playerIndex the number of the current player
	 */
	private static void viewPlayers(int playerNumber, SimpleGameData data, int playerIndex) {
		StringBuilder str = new StringBuilder("\n");
		str.append("My hint : \n- ");
		for (Hint h:data.getPlayers().get(playerIndex).getHints()) {
			str.append(nullOrPrintable(h.getNumber())).append(" ").append(nullOrPrintable(h.getColor())).append(" - ");
		}
		str.append("\n");
		
		for (int index = 0; index < playerNumber; index++) {
			if (index != playerIndex) {	// We don't want to show the current player's cards
				str.append("\nPlayer ").append(index + 1).append(" : \n- ");
				for (Card c:data.getPlayers().get(index).getHand().getCards()) {
					str.append(c.getNumber()).append(" ").append(c.getColor()).append(" - ");
				}
				str.append("\n");
			}
		}
		System.out.println(str.toString());
	}
	
	
	/**
	 * Prints the whole game
	 * @param playerNumber the number of players in the game
	 * @param data the data of the current game
	 * @param playerIndex the number of the current player
	 */
	public static void draw(int playerNumber, SimpleGameData data, int playerIndex) {
		Player.testNumberPlayer(playerNumber);
		Player.testIndexPlayer(playerIndex, playerNumber);
		Objects.requireNonNull(data, "The GameData can't be null");
		
		viewGameboard(data);
		viewPlayers(playerNumber, data, playerIndex);
	}

	
	/**
	 * Shows the final score at the end of the game
	 * @param score the game score
	 */
	public static void endGame(int score) {
		System.out.println("\nGame ends with score: " + score);
	}
	
	
	/**
	 * Draws the current state of the playing board
	 * @param board the current board
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void viewBoard(GameBoard board, Graphics2D graphics) {
		var pack = board.getBoard().getCards();
		var x = GraphicsData.boardCardPosX;
		var y = GraphicsData.boardCardPosY;
		var posX = 0;
		var posY = 0;
		var cardWidth = GraphicsData.boardCardWidth;
		var cardHeight = GraphicsData.boardCardHeight;
		
		for (var number = 1; number <= 5; number++) {
			posY = y * (number - 1);
			for (var color = 0; color < CardColor.numberColor(); color++) {
				posX = (int) (x + (cardWidth + (10 * SimpleGameView.RATIO_WIDTH)) * color);
				if (pack.get(color) != null && pack.get(color).getNumber() >= number) {
					graphics.drawImage(GraphicsData.getCardsimage().get(color).get(number - 1), posX, posY, cardWidth, cardHeight, null);
				}
			}
		}
	}
	
	
	/**
	 * Draws the coins left
	 * @param coin the coins we want to draw
	 * @param image the image corresponding to the coins
	 * @param posX the X coordinate of the coin
	 * @param posY the Y coordinate of the coin
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void viewCoin(Coin coin, BufferedImage image, int posX, int posY, Graphics2D graphics) {
		var stringHeight = 0;		
		graphics.setColor(new Color(20, 20, 20));
		graphics.fill(new Rectangle2D.Float(posX - (75 * SimpleGameView.RATIO_WIDTH), posY, GraphicsData.coinsSize + (85 * SimpleGameView.RATIO_WIDTH), GraphicsData.coinsSize + (10 * SimpleGameView.RATIO_HEIGHT)));
		graphics.drawImage(image, posX, posY, GraphicsData.coinsSize, GraphicsData.coinsSize, null);
		int fontSize = (int) (40 * SimpleGameView.RATIO_WIDTH);
		Font font = new Font("Serif", Font.PLAIN, fontSize);
		graphics.setFont(font);
		
		FontMetrics metrics = graphics.getFontMetrics(font);
		stringHeight = posY + ((GraphicsData.coinsSize - metrics.getHeight()) / 2) + metrics.getAscent();	// We want to center the text in relation to the coin's image
		
		graphics.setColor(Color.WHITE);
		graphics.drawString(Integer.toString(coin.getNumber()), posX - (40 * SimpleGameView.RATIO_WIDTH), stringHeight);
	}
	

	/**
	 * Draws the discard pile (The last discarded card, so the topmost card on the discard pile)
	 * @param board the current board
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void viewLastDiscard(GameBoard board, Graphics2D graphics) {
		var cardWidth = GraphicsData.discardCardWidth;
		var cardHeight = GraphicsData.discardCardHeight;
		var card = board.lastDiscard();
		if (card != null) {
			var color = card.getColor();
			graphics.drawImage(GraphicsData.getCardsimage().get(CardColor.fromColor(color)).get(card.getNumber() - 1), width - cardWidth, height - cardHeight, cardWidth, cardHeight, null);
		}
	}


	/**
	 * Draws the entirety of the gameboard
	 * @param data the data of the current game
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void viewGameBoard(SimpleGameData data, Graphics2D graphics) {
		var board = data.getGameBoard();
		
		viewLastDiscard(board, graphics);
		viewCoin(board.getBlue(), GraphicsData.getBlueCoinImage(), GraphicsData.coinsPosX, GraphicsData.coinsPosY, graphics);
		viewCoin(board.getRed(), GraphicsData.getRedCoinImage(), GraphicsData.coinsPosX, (int) (GraphicsData.coinsPosY + GraphicsData.coinsSize + 20 * SimpleGameView.RATIO_HEIGHT), graphics);
		viewBoard(board, graphics);
	}
	
	/**
	 * Draw a message if it's the last turn of the player on the screen
	 * @param data the data of the current game
	 * @param context 
	 * @throws InterruptedException 
	 */
	private static void viewLastTurnMessage(Graphics2D graphics) {
		drawMessage("It's your last turn", graphics, height / 2);
	}
	
	private static void manageLastTurnMessage(SimpleGameData data, ApplicationContext context) {
		if (data.getGameBoard().deckSize() == 0) {
			context.renderFrame(graphics -> viewLastTurnMessage(graphics));
			SimpleGameController.waitEvent(context);
		}
	}
	
	/**
	 * Draws what the current player knows (His hints and the others players' cards)
	 * @param playerNumber the number of player in the game
	 * @param data the GameData containing the game data
	 * @param playerIndex the current player
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void viewPlayers(int playerNumber, SimpleGameData data, int playerIndex, Graphics2D graphics) {
		var cardsInHand = 0;
		var currentPlayer = 0;
		
		for (int index = 0; index < playerNumber; index++) {
			cardsInHand = data.getPlayers().get(index).sizeOfHand();
			
			if (index != playerIndex) {	// We show every other player's cards
				drawPlayersCards(graphics, data, index, currentPlayer);
				currentPlayer++;
				
			} else if (index == playerIndex) {	// We don't want to show the current player's cards
				drawCurrentPlayerCards(graphics, data, index, cardsInHand);
			}
		}
	}
	
	
	/**
	 * Draws the game board from its data
	 * @param playerNumber the number of player in the game
	 * @param data the GameData containing the game data
	 * @param playerIndex the current player
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void draw(int playerNumber, SimpleGameData data, int playerIndex, Graphics2D graphics) {
		Player.testNumberPlayer(playerNumber);
		Player.testIndexPlayer(playerIndex, playerNumber);
		Objects.requireNonNull(data, "The GameData can't be null");
		
		LinkedHashMap<String, Color> map = new LinkedHashMap<String, Color>();
		map.put("Play", new Color(205, 205, 205));
		map.put("Discard", new Color(205, 205, 205));
		map.put("Hint", new Color(205, 205, 205));
		
		graphics.setColor(new Color(20, 20, 20));
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		
		viewGameBoard(data, graphics);
		viewPlayers(playerNumber, data, playerIndex, graphics);
		viewMenu(graphics, map);
	}

	
	
	/**
	 * Renders the current frame of the game board by using its data
	 * @param playerNumber the number of player in the game
	 * @param data the GameData containing the game data
	 * @param playerIndex the current player
	 * @param context the {@code ApplicationContext} of the game
	 */
	public static void draw(int playerNumber, SimpleGameData data, int playerIndex, ApplicationContext context) {
		manageLastTurnMessage(data, context);
		context.renderFrame(graphics -> draw(playerNumber, data, playerIndex, graphics));
	}
	
	
	/**
	 * Draws the others players' cards
	 * @param graphics a Graphics2D object provided by the default method
	 * @param data the GameData containing the game data
	 * @param index the index of the player we want to draw the cards of
	 * @param currentPlayer the number of the player we want to draw the cards of, not counting the player who is currently playing
	 */
	private static void drawPlayersCards(Graphics2D graphics, SimpleGameData data, int index, int currentPlayer) {
		var currentCard = 0;
		var x = GraphicsData.playersCardPosX;
		var y = GraphicsData.playersCardPosY;
		var posX = 0;
		var posY = 0;
		var cardWidth = GraphicsData.playersCardWidth;
		var cardHeight = GraphicsData.playersCardHeight;
		
		posY = (int) (30 * SimpleGameView.RATIO_HEIGHT + (y + 30 * SimpleGameView.RATIO_HEIGHT) * currentPlayer);
		
		for (Card card : data.getPlayers().get(index).getHand().getCards()) {
			posX = x * currentCard;
			CardColor color = card.getColor();
			
			graphics.drawImage(GraphicsData.getCardsimage().get(CardColor.fromColor(color)).get(card.getNumber() - 1), posX, posY, cardWidth, cardHeight, null);		
			currentCard++;
		}
		drawPlayersNumber(graphics, index, 0, (y + 30 * SimpleGameView.RATIO_HEIGHT) * currentPlayer, x * currentCard, (int) (30 * SimpleGameView.RATIO_HEIGHT), 20);
	}
	
	
	/**
	 * Draws the number of the player above his cards
	 * @param graphics a Graphics2D object provided by the default method
	 * @param playerIndex the index of the player we want to draw
	 * @param posX the X coordinate of the player number
	 * @param posY the Y coordinate of the player number
	 * @param fontSize the size of the font to use
	 */
	private static void drawPlayersNumber(Graphics2D graphics, int playerIndex, float posX, float posY, int buttonWidth, int buttonHeight, int fontSize) {
		var string = new StringBuilder();
		string.append("Player ").append(playerIndex + 1);

		Font font = new Font("Serif", Font.PLAIN, fontSize);
		FontMetrics metrics = graphics.getFontMetrics(font);
		var stringWidth = posX + (buttonWidth - metrics.stringWidth(string.toString())) / 2;
		var stringHeight = posY + ((buttonHeight - metrics.getHeight()) / 2) + metrics.getAscent();
		graphics.setFont(font);
		graphics.drawString(string.toString(), stringWidth, stringHeight);
	}
	
	
	/**
	 * Draws the player's cards with hints. Here, the hints are depicted with images
	 * @param graphics a Graphics2D object provided by the default method
	 * @param data the GameData containing the game data
	 * @param index the index of the player we want to draw the cards of
	 * @param cardsInHand the number of cards the player has
	 * @param width the width of the canvas
	 */
	private static void drawCurrentPlayerCards(Graphics2D graphics, SimpleGameData data, int index, int cardsInHand) {
		var j = 0;
		var posY = GraphicsData.currentCardPosY;
		var posX = 0;
		var cardWidth = GraphicsData.currentCardWidth;
		var cardHeight = GraphicsData.currentCardHeight;
		var x = (int) ((width - cardWidth * cardsInHand - (cardsInHand - 1)) / 2);
		
		for (Hint hint : data.getPlayers().get(index).getHints()) {
			var color = hint.getColor();
			var number = hint.getNumber();
			posX = (int) (x + (cardWidth + (4 * SimpleGameView.RATIO_WIDTH)) * j);
			
			if (color != null && number != null) {	// We know the card thanks to the hints
				graphics.drawImage(GraphicsData.getCardsimage().get(CardColor.fromColor(color)).get(number - 1), posX, posY, cardWidth, cardHeight, null);
			} else if (color != null && number == null) {	// We know the color of the card
				graphics.drawImage(GraphicsData.getCardsColorHintImage().get(CardColor.fromColor(color)), posX, posY, cardWidth, cardHeight, null);
			} else if (color == null && number != null) {	// We know the number of the card
				graphics.drawImage(GraphicsData.getCardsNumberHintImage().get(number - 1), posX, posY, cardWidth, cardHeight, null);
			} else {	// We know nothing of the card
				graphics.drawImage(GraphicsData.getCardBack(), posX, posY, cardWidth, cardHeight, null);
			}
			j++;
		}
		drawPlayersNumber(graphics, index, 0, GraphicsData.currentCardPosY, (int) (420 * SimpleGameView.RATIO_WIDTH), (int) (50 * SimpleGameView.RATIO_HEIGHT), (int) (40 * SimpleGameView.RATIO_HEIGHT));
	}
	
	
	/**
	 * Draws the button the player can use
	 * @param graphics a Graphics2D object provided by the default method
	 * @param e a LinkedHashMap entry with the button's label and color
	 * @param x the x coordinates of the button
	 * @param y the y coordinates of the button
	 * @param buttonWidth the button's width
	 * @param buttonHeight the button's height
	 */
	private static void viewButton(Graphics2D graphics, Entry<String, Color> e, int x, int y, int buttonWidth, int buttonHeight, int fontSize) {
		var stringWidth = 0;
		var stringHeight = 0;
		graphics.setColor(e.getValue());
		graphics.fill(new Rectangle2D.Float(x, y, buttonWidth, buttonHeight));
		
		Font font = new Font("Serif", Font.PLAIN, fontSize);
		FontMetrics metrics = graphics.getFontMetrics(font);
		stringWidth = x + (buttonWidth - metrics.stringWidth(e.getKey())) / 2;
		stringHeight = y + ((buttonHeight - metrics.getHeight()) / 2) + metrics.getAscent();
		
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		graphics.drawString(e.getKey(), stringWidth, stringHeight);
	}
	
	
	/**
	 * Draws the menu containing all the action's buttons
	 * @param graphics a Graphics2D object provided by the default method
	 * @param map a LinkedHashMap to store the buttons' label and color
	 */
	protected static void viewMenu(Graphics2D graphics, LinkedHashMap<String, Color> map) {
		var i = 0;
		var j = 0;
		var posX = 0;
		var posY = 0;
		
		graphics.setColor(new Color(20, 20, 20));
		graphics.fill(new Rectangle2D.Float(0, GraphicsData.currentCardPosY + 65 * SimpleGameView.RATIO_HEIGHT, 420 * SimpleGameView.RATIO_WIDTH, GraphicsData.currentCardHeight));
		
		for (Entry<String, Color> e : map.entrySet()) {
			if (i == 3) {
				j++;
				i = 0;
			}
			posX = GraphicsData.buttonPosX + i * (GraphicsData.buttonWidth + GraphicsData.buttonPosX);
			posY = (int) (GraphicsData.buttonPosY + j * (100 * SimpleGameView.RATIO_HEIGHT));
			viewButton(graphics, e, posX, posY, GraphicsData.buttonWidth, GraphicsData.buttonHeight, (int) (20 * SimpleGameView.RATIO_HEIGHT));
			i++;
		}
	}
	
	/**
	 * Draws a message on the game screen
	 * @param string to print
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void drawMessage(String string, Graphics2D graphics, int position) {
		var color = new Color(40, 40, 40, 200);
		int fontSize = (int) (60 * SimpleGameView.RATIO_WIDTH);
		Font font = new Font("Serif", Font.PLAIN, fontSize);
		FontMetrics metrics = graphics.getFontMetrics(font);
		
		var stringWidth = (width - metrics.stringWidth(string.toString())) / 2;
		
		graphics.setFont(font);
		graphics.setColor(color);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.WHITE);
		graphics.drawString(string, stringWidth, position);
	}
	
	/**
	 * Draws the end game screen
	 * @param score the current score of the game
	 * @param graphics a Graphics2D object provided by the default method
	 */
	private static void drawEndGame(int score, Graphics2D graphics) {
		var string = new StringBuilder();
		string.append("Score : ").append(score);
		
		drawMessage(string.toString(), graphics, height / 3);
		drawMessage(saveEndGameMessage(score, graphics), graphics, height / 2);
	}
	
	
	/**
	 * Saves the message corresponding to the score
	 * @param score the score of the game
	 * @param graphics a Graphics2D object provided by the default method
	 * @return the message for the score
	 */
	private static String saveEndGameMessage(int score, Graphics2D graphics) {
		if (score <= 5) {
			return "What a shame, your show was dreadful... I am out of words...";
		} else if (score <= 10) {
			return "Mediocre, lucky of you to get some applause...";
		} else if (score <= 15) {
			return "Respectable, but will not live long enough in memories...";
		} else if (score <= 20) {
			return "Wonderful, the crowd will be going home happy.";
		} else if (score <= 24) {
			return "Amazing, will definitely live on as an excellent show !";
		} else {
			return "Astonishingly beautiful ! This firework will live on as a legendary one !";
		}
	}
	
	
	/**
	 * Shows the final score at the end of the game
	 * @param score the game score
	 * @param context the {@code ApplicationContext} of the game
	 */	
	public static void endGame(int score, ApplicationContext context) {
		context.renderFrame(graphics -> drawEndGame(score, graphics));
	}
	
	
	/**
	 * Draws the main screen of the game to select the number of players
	 * @param graphics a Graphics2D object provided by the default method
	 * @param context the {@code ApplicationContext} of the game
	 */
	public static void drawMainScreen(Graphics2D graphics, ApplicationContext context) {
		int fontSize = (int) (45 * SimpleGameView.RATIO_WIDTH);
		Font font = new Font("Serif", Font.PLAIN, fontSize);
		FontMetrics metrics = graphics.getFontMetrics(font);
		LinkedHashMap<String, Color> map = saveNumberOfPlayers(graphics);
		
		var string = new StringBuilder();
		string.append("Number of players");
		
		var stringWidth = ((width - metrics.stringWidth(string.toString())) / 2);

		graphics.setColor(new Color(20, 20, 20));
		graphics.setFont(font);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(new Color(33, 84, 138));
		graphics.fill(new Rectangle2D.Float(GraphicsData.menuPosX, GraphicsData.menuPosY, GraphicsData.menuWidth, GraphicsData.menuHeight));
		graphics.setColor(Color.WHITE);
		graphics.drawString(string.toString(), stringWidth, GraphicsData.menuPosY + metrics.getAscent());
		
		fontSize = (int) (45 * SimpleGameView.RATIO_HEIGHT);
		font = new Font("Serif", Font.PLAIN, fontSize);
		metrics = graphics.getFontMetrics(font);
		
		drawNumberOfPlayers(graphics, map, fontSize, metrics, context);
	}
	
	
	/**
	 * Draws the buttons for the number of players in the main screen
	 * @param graphics a Graphics2D object provided by the default method
	 * @param map a LinkedHashMap containing the label and the color of each buttons
	 * @param metrics a FontMetrics object containing informations on the font used
	 * @param context the {@code ApplicationContext} of the game
	 */
	private static void drawNumberOfPlayers(Graphics2D graphics, LinkedHashMap<String, Color> map, int fontSize, FontMetrics metrics, ApplicationContext context) {
		var i = 0;
		var posY = 0;
		for (Entry<String, Color> e : map.entrySet()) {
			posY = (int) (GraphicsData.menuPosY + i * (GraphicsData.menuButtonHeight + 30 * SimpleGameView.RATIO_HEIGHT) + metrics.getAscent() + 50 * SimpleGameView.RATIO_HEIGHT);
			viewButton(graphics, e, GraphicsData.menuButtonPosX, posY, GraphicsData.menuButtonWidth, GraphicsData.menuButtonHeight, fontSize);
			i++;
		}
	}
	
	
	/**
	 * Creates a LinkedHashMap containing the label and the color for each buttons
	 * @param graphics a Graphics2D object provided by the default method
	 * @return the LinkedHashMap containing the label and the color of each buttons
	 */
	private static LinkedHashMap<String, Color> saveNumberOfPlayers(Graphics2D graphics) {
		var i = 0;
		LinkedHashMap<String, Color> map = new LinkedHashMap<String, Color>();
		
		for (i = 2; i < 6; i++) {
			map.put(Integer.toString(i), new Color(133, 19, 19));
		}
		return map;
	}
	
	
	/**
	 * Gets the height of the window
	 * @return the height of the window
	 */
	public static int getHeight() {
		return height;
	}
	
	
	/**
	 * Gets the width of the window
	 * @return the width of the window
	 */
	public static int getWidth() {
		return width;
	}
}
