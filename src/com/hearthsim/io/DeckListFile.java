package com.hearthsim.io;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidCardException;

public class DeckListFile {

	Deck deck_;
	
	/**
	 * Constructor
	 * 
	 * @param setupFilePath The file path to the deck list to be read
	 * @throws IOException 
	 */
	DeckListFile(Path setupFilePath) throws HSInvalidCardException, IOException {
		this.read(setupFilePath);
	}
	
	/**
	 * Read the given file
	 * 
	 * @param setupFilePath The file path to the deck list to be read
	 * @throws IOException 
	 */
	void read(Path setupFilePath) throws HSInvalidCardException, IOException {
		String inStr = new String(Files.readAllBytes(setupFilePath));
		this.parseDeckList(inStr);
	}
	
	/**
	 * Parse a given deck list string and construct a Deck object out of it
	 * 
	 * @param deckListStr
	 */
	void parseDeckList(String deckListStr) throws HSInvalidCardException {
		String[] deckList = deckListStr.split(",");
		ArrayList<Card> cards = new ArrayList<Card>();
		//Ignore the first entry for now... hero classes aren't implemented yet!
		for (int indx = 1; indx < deckList.length; ++indx) {
			try {
				String cleanedString = deckList[indx].replace(" ", "").replace("'", "");
				Class<?> clazz = Class.forName(cleanedString);
				Constructor<?> ctor = clazz.getConstructor();
				Object object = ctor.newInstance();
				cards.add((Card)object);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
				throw new HSInvalidCardException();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
				throw new HSInvalidCardException();
			}
		}
		deck_ = new Deck(cards);
	}

	/**
	 * Get the deck
	 * @return
	 */
	public Deck getDeck() {
		return deck_;
	}
}
