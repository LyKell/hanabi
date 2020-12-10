package fr.umlv.hanabi.mvc;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.hanabi.graphic.GraphicsData;
import fr.umlv.hanabi.player.Player;
import fr.umlv.hanabi.player.PlayerAction;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;

public class SimpleGameController {
	/**
	 * Ask the user for an int 
	 * @param input the scanner which is parse
	 * @param message the message print to ask the integer
	 * @return the int entered
	 */
	static int askForInt(Scanner input, String message) {
		int askInt;
		try{
			System.out.println(message);
			askInt = Integer.parseInt(input.next());
		} catch (NumberFormatException e){
			System.out.println("Please enter a number!");
			askInt = askForInt(input, message);
		}
		return askInt;
	}
	
	
	/**
	 * Ask the user for a String 
	 * @param input the scanner which is parse
	 * @param message the message print to ask the integer
	 * @return the String entered
	 */
	static String askForString(Scanner input, String message) {
		String askString;
		System.out.println(message);
		askString = input.next();
		return askString;
	}
	
	
	/**
	 * Decides what the player wants to do
	 * @param playerNumber the number of players in the game
	 * @param data all the data concerning the current game
	 * @param playerIndex the current player's number
	 * @param input the scanner which is parsed
	 * @return the updated data of the game
	 */
	public static SimpleGameData playerTurn(int playerNumber, SimpleGameData data, int playerIndex, Scanner input) {
		Player.testNumberPlayer(playerNumber);
		Player.testIndexPlayer(playerIndex, playerNumber);
		Objects.requireNonNull(data, "The GameData can't be null");
		Objects.requireNonNull(input, "A scanner cannot be null");
		
		String action;
		boolean result;
		
		SimpleGameView.draw(playerNumber, data, playerIndex);
		do {
			action = askForString(input, "\nDiscard (d), Play (p),Hint (h) ?");
			result = data.playerAction(playerNumber, playerIndex, action, input);
		} while (result == false);
		return data;
	}
		
	
	/**
	 * Chooses the game type we want
	 * @param gameType an int that defines if we are playing in the console or in a graphic window
	 * @param score the score of the game
	 * @param input the scanner which is parsed
	 */
	public static void choseGame(int gameType, int score, Scanner input) {
		if (gameType == 1) {	// Console
			consoleGame(score, input);
		} else if (gameType == 2) {	// Graphic
			graphicGame();
		}
	}
	
	
	/**
	 * Manages a complete game in the console
	 * @param score the score of the game
	 * @param input the scanner which is parsed
	 */
	public static void consoleGame(int score, Scanner input) {
		var playerNumber = 0;
		do {
			playerNumber = askForInt(input, "Input the number of players (2 - 5) :");
		} while (playerNumber < 2 || playerNumber > 5);

		score = fullGame(playerNumber, null, input);
		SimpleGameView.endGame(score);
	}
	
	
	/**
	 * Manages a complete game in a graphic window, from player selection to end game
	 */
	public static void graphicGame() {
		Application.run(new Color(20, 20, 20), context -> {
			ScreenInfo screenInfo = context.getScreenInfo();
			int width = (int) screenInfo.getWidth();
			int height = (int) screenInfo.getHeight();
			int score = -1;
			var playerNumber = 0;
			
			new SimpleGameView(width, height);
			context.renderFrame(graphics -> SimpleGameView.drawMainScreen(graphics, context));
			playerNumber = SimpleGameData.retrieveNumberOfPlayers(context, GraphicsData.menuButtonPosX, GraphicsData.menuButtonWidth, GraphicsData.menuButtonHeight);
			score = fullGame(playerNumber, context, null);
			SimpleGameView.endGame(score, context);
			waitEvent(context);	
			context.exit(0);
		});
	}
	
	
	/**
	 * Manages a complete game
	 * @param playerNumber the number of players in the game
	 * @param context the {@code ApplicationContext} of the game
	 * @param input the parsed scanner
	 * @return the score of the game
	 */
	private static int fullGame(int playerNumber, ApplicationContext context, Scanner input) {
		SimpleGameData data = new SimpleGameData(playerNumber);
		int score = -1;
		
		while (score < 0) {
			for (int playerIndex = 0; playerIndex < playerNumber; playerIndex++) {
				if (context == null) {
					data = playerTurn(playerNumber, data, playerIndex, input);
				} else if (input == null) {
					data = playerTurn(playerNumber, data, playerIndex, context);
				}

				score = data.endGame();
				if (score == 0) {
					return score;
				}
				if (score == 25) {
					return score;
				}
			}
		}
		return score;
	}
	
	
	/**
	 * Manages the action to quit the game
	 * @param context the {@code ApplicationContext} of the game
	 */
	protected static void waitEvent(ApplicationContext context) {
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			Action action = event.getAction();
			if (action == Action.POINTER_DOWN) {
				return;
			}
		}
	}
	
	
	/**
	 * Decides what the player wants to do
	 * @param playerNumber the number of players in the game
	 * @param data all the data concerning the current game
	 * @param playerIndex the current player's number
	 * @param context the {@code ApplicationContext} of the game
	 * @return the updated data of the game
	 */
	public static SimpleGameData playerTurn(int playerNumber, SimpleGameData data, int playerIndex, ApplicationContext context) {
		Player.testNumberPlayer(playerNumber);
		Player.testIndexPlayer(playerIndex, playerNumber);
		Objects.requireNonNull(data, "The GameData can't be null");
		
		Point2D.Float location;
		boolean result;
		var selection = -1;
		var action = PlayerAction.Invalid;
		
		do {
			SimpleGameView.draw(playerNumber, data, playerIndex, context);
			location = retrieveMouseClick(context);
			selection = data.recoverButtonsLocation(location, 3, 0, 0) - 1;
			action = PlayerAction.convertIntToAction(selection);
			result = data.playerAction(playerNumber, playerIndex, action, context);
		} while (result == false);
		
		return data;
	}
	
	
	/**
	 * Retrieves the mouse click on the screen
	 * @param context the {@code ApplicationContext} of the game
	 * @return the location of the mouse click
	 */
	public static Point2D.Float retrieveMouseClick(ApplicationContext context) {
		Point2D.Float location;
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			
			Action action = event.getAction();
			if (action == Action.POINTER_DOWN) {
				location = event.getLocation();
				return location;
			}
		}
	}
	
	
	/**
	 * Decides the number of player and manages each player turn
	 */
	public static void simpleGame() {
		var input = new Scanner(System.in);
		int gameType;
		var score = -1;
		
		do {
			gameType = askForInt(input, "Input the game type you want (1 - Console, 2 - Graphic) : ");
		} while (gameType != 1 && gameType != 2);
		
		choseGame(gameType, score, input);
		
		input.close();
	}
	
	
	public static void main(String[] args) {
		simpleGame();
	}
}
