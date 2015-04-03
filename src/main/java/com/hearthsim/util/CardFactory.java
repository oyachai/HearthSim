package com.hearthsim.util;

import com.hearthsim.card.Card;
import com.hearthsim.exception.HSInvalidCardException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
            try {
                Class<?> clazz = Class.forName("com.hearthsim.card.spellcard.concrete." + cleanedString);
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance();
                return (Card)object;
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException e2) {
                throw new HSInvalidCardException("Unknown card: " + cleanedString);
            }
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new HSInvalidCardException("Unknown card: " + cleanedString);
        }
    }
}
