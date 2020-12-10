package fr.umlv.hanabi.mvc;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import fr.umlv.hanabi.card.CardColor;

public class GraphicsData {
	private static final ArrayList<ArrayList<Image>> CARDS_IMAGE;
	private static final ArrayList<Image> CARDS_COLOR_HINT_IMAGE;
	private static final ArrayList<Image> CARDS_NUMBER_HINT_IMAGE;
	private static BufferedImage CARD_BACK;
	private static BufferedImage RED_COIN_IMAGE;
	private static BufferedImage BLUE_COIN_IMAGE;
	protected static final int DEFAULT_WIDTH = 1920;
	protected static final int DEFAULT_HEIGHT = 1080;
		
	protected final static int boardCardWidth = (int) (215 * SimpleGameView.RATIO_WIDTH);
	protected final static int boardCardHeight = (int) (300 * SimpleGameView.RATIO_HEIGHT);
	protected final static int boardCardPosX = (int) (650 * SimpleGameView.RATIO_WIDTH);
	protected final static int boardCardPosY = (int) (80 * SimpleGameView.RATIO_HEIGHT);
	
	protected final static int discardCardWidth = (int) (179 * SimpleGameView.RATIO_WIDTH);
	protected final static int discardCardHeight = (int) (250 * SimpleGameView.RATIO_HEIGHT);
	
	protected final static int coinsSize = (int) (75 * SimpleGameView.RATIO_WIDTH);
	protected final static int coinsPosY = (int) (590 * SimpleGameView.RATIO_HEIGHT);
	protected final static int coinsPosX = (int) (SimpleGameView.getWidth() - (coinsSize + 10 * SimpleGameView.RATIO_WIDTH));
	
	protected final static int playersCardWidth = (int) (108 * SimpleGameView.RATIO_WIDTH);
	protected final static int playersCardHeight = (int) (150 * SimpleGameView.RATIO_HEIGHT);
	protected final static int playersCardPosX = (int) (112 * SimpleGameView.RATIO_WIDTH);
	protected final static int playersCardPosY = (int) (155 * SimpleGameView.RATIO_HEIGHT);
	
	protected final static int currentCardWidth = (int) (215 * SimpleGameView.RATIO_WIDTH);
	protected final static int currentCardHeight = (int) (300 * SimpleGameView.RATIO_HEIGHT);
	protected final static int currentCardPosY = (int) (780 * SimpleGameView.RATIO_HEIGHT);
	
	protected final static int buttonWidth = (int) (128 * SimpleGameView.RATIO_WIDTH);
	protected final static int buttonHeight = (int) ((buttonWidth / 2));
	protected final static int buttonPosX = (int) (9 * SimpleGameView.RATIO_WIDTH);
	protected final static int buttonPosY = (int) ((currentCardPosY - (100 * SimpleGameView.RATIO_HEIGHT - buttonHeight) / 2) + currentCardHeight / 2 - buttonHeight);
	
	protected final static float menuWidth = (float) SimpleGameView.getWidth() / 5;
	protected final static float menuHeight = (float) (3 * SimpleGameView.getHeight()) / 5;
	protected final static float menuPosX = (float) (2 * SimpleGameView.getWidth()) / 5;
	protected final static float menuPosY = (float) SimpleGameView.getHeight() / 5;
	
	protected final static int menuButtonWidth = (int) ((GraphicsData.menuWidth - 20 * SimpleGameView.RATIO_WIDTH));
	protected final static int menuButtonHeight = (int) (113 * SimpleGameView.RATIO_HEIGHT);
	protected final static int menuButtonPosX = (int) ((GraphicsData.menuPosX + 10 * SimpleGameView.RATIO_WIDTH));
	
	
	static {	// Static initialization block in order to use methods to initialize all the static fields
		try {
			CARD_BACK = initCardBack();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (CARD_BACK == null) {
			throw new IllegalStateException("Failed to initialize card's back image");
		}
		
		CARDS_IMAGE = new ArrayList<ArrayList<Image>>();
		CARDS_COLOR_HINT_IMAGE = new ArrayList<Image>();
		CARDS_NUMBER_HINT_IMAGE = new ArrayList<Image>();
		
		RED_COIN_IMAGE = initAndCheckCoinsImage(CardColor.Red);
		BLUE_COIN_IMAGE = initAndCheckCoinsImage(CardColor.Blue);
		initAndCheckEachCardsImage();
	}
	
	
	/**
	 * Initializes and checks if the coin image has been correctly initialized
	 * @param  the static field where we want to save the image
	 * @param color the color of the coins
	 */
	private static BufferedImage initAndCheckCoinsImage(CardColor color) {
		BufferedImage tmpImage = null;
		try {
			tmpImage = initCoinsImage(color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmpImage;
	}
	
	
	/**
	 * Initializes and checks if each cards' image has been correctly initialized
	 */
	private static void initAndCheckEachCardsImage() {
		try {
			initEachCardsImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initializes an image for cardBack field
	 * @return the image for cardBack
	 * @throws IOException 
	 */
	private static BufferedImage initCardBack() throws IOException {
		InputStream input = ClassLoader.getSystemResourceAsStream("resources/Card_Back.png");
		BufferedImage image = null;
		
		if (input == null) {
			throw new IOException("Image file failed to load");
		}
		
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		input.close();
		return image;
	}
	
	
	/**
	 * Initializes an image for every kind of cards in the game
	 * @throws IOException 
	 */
	private static void initEachCardsImage() throws IOException {
		StringBuilder folderDirectory = new StringBuilder();
		StringBuilder hintColorDirectory = new StringBuilder();
		StringBuilder hintNumberDirectory = new StringBuilder();
		
		for (int i = 0; i < CardColor.numberColor(); i++) {	// We get the folders of the images -> See how /resources folder is built
			folderDirectory.append("resources/Card_").append(CardColor.fromInt(i)).append("/");
			hintColorDirectory.append(folderDirectory).append("Card_Hint.png");
			hintNumberDirectory.append("resources/Hint/Hint_").append(i + 1).append(".png");
			
			CARDS_IMAGE.add(new ArrayList<Image>());
			
			addEachCardsImages(folderDirectory, i);
			initEachHintsImage(CARDS_COLOR_HINT_IMAGE, hintColorDirectory);
			initEachHintsImage(CARDS_NUMBER_HINT_IMAGE, hintNumberDirectory);
			
			hintColorDirectory = new StringBuilder();
			hintNumberDirectory = new StringBuilder();
			folderDirectory = new StringBuilder();	// Reset the StringBuilder to get a new directory
			
		}
	}
	
	
	/**
	 * Initializes an image for a hint
	 * @param imageList the ArrayList where we want to save the images
	 * @param directory the file name of the hint's image
	 * @throws IOException 
	 */
	private static void initEachHintsImage(ArrayList<Image> imageList, StringBuilder directory) throws IOException {
		InputStream input = ClassLoader.getSystemResourceAsStream(directory.toString());
		if (input == null) {
			throw new IOException("Image file failed to load");
		}
		
		try {
			imageList.add(ImageIO.read(input));
		} catch (IOException e) {
			e.printStackTrace();
		}
		input.close();
	}
	
	
	/**
	 * Initializes an image for the coins
	 * @param color the color of the coin
	 * @return a BufferedImage of the coin
	 * @throws IOException 
	 */
	private static BufferedImage initCoinsImage(CardColor color) throws IOException {
		InputStream input;
		var directory = new StringBuilder();
		BufferedImage image = null;
		
		directory.append("resources/Coins/");
		
		if (CardColor.fromColor(color) == CardColor.fromColor(CardColor.Red)) {	// Red coins
			directory.append("Coin_Red.png");
		} else if (CardColor.fromColor(color) == CardColor.fromColor(CardColor.Blue)) {	// Blue coins
			directory.append("Coin_Blue.png");
		}
		
		input = ClassLoader.getSystemResourceAsStream(directory.toString());
		if (input == null) {
			throw new IOException("Image file failed to load");
		}
		
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		input.close();
		return image;
	}
	
	
	/**
	 * Adds new cards images from folderDirectory to the ArrayListe cardsImage
	 * @param folderDirectory the folder where we search for the images
	 * @param i the current converted color of the card
	 * @throws IOException
	 */
	private static void addEachCardsImages(StringBuilder folderDirectory, int i) throws IOException {
		InputStream input = null;
		var imageDirectory = new StringBuilder();
		
		for (int j = 1; j <= 5; j++) {	// We get every image from the current folder
			imageDirectory.append(folderDirectory).append("Card_0").append(j).append(".png");
			
			input = ClassLoader.getSystemResourceAsStream(imageDirectory.toString());
			if (input == null) {
				throw new IOException("Image file failed to load");
			}
			
			try {
				CARDS_IMAGE.get(i).add(ImageIO.read(input));
			} catch (IOException e) {
				e.printStackTrace();
			}
			imageDirectory = new StringBuilder();
		}
		input.close();
	}
	
	
	/**
	 * Gets the blue coins image
	 * @return the blue coins image
	 */
	public static BufferedImage getBlueCoinImage() {
		return BLUE_COIN_IMAGE;
	}
	
	
	/**
	 * Gets the cards' back image
	 * @return the card back image
	 */
	public static BufferedImage getCardBack() {
		return CARD_BACK;
	}
	
	
	/**
	 * Gets the red coins image
	 * @return the red coins image
	 */
	public static BufferedImage getRedCoinImage() {
		return RED_COIN_IMAGE;
	}
	
	
	/**
	 * Gets the color hint image
	 * @return the color hint image
	 */
	public static ArrayList<Image> getCardsColorHintImage() {
		return CARDS_COLOR_HINT_IMAGE;
	}
	
	
	/**
	 * Gets the cards image
	 * @return the cards image
	 */
	public static ArrayList<ArrayList<Image>> getCardsimage() {
		return CARDS_IMAGE;
	}
	
	
	/**
	 * Gets the number hint image
	 * @return the number hint image
	 */
	public static ArrayList<Image> getCardsNumberHintImage() {
		return CARDS_NUMBER_HINT_IMAGE;
	}
}
