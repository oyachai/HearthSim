package com.hearthsim.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.hearthsim.card.Card;
import com.hearthsim.exception.HSInvalidCardException;

/**
 * A utility class to convert a card name into a Card class instance
 *
 */
public class CardFactory {

	public static Card getCard(String cardName) throws HSInvalidCardException {
		String cleanedString = cardName.trim();
		try {
			Class<?> clazz = Class.forName("com.hearthsim.card.minion.concrete." + cleanedString);
			Constructor<?> ctor = clazz.getConstructor();
			Object object = ctor.newInstance();
			return (Card)object;
		} catch (ClassNotFoundException e) {
			throw new HSInvalidCardException("Unknown card: " + cleanedString);
		} catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
			throw new HSInvalidCardException("Unknown card: " + cleanedString);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			throw new HSInvalidCardException("Unknown card: " + cleanedString);
		}
	}
}
