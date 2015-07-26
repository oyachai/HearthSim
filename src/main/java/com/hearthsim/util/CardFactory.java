package com.hearthsim.util;

import com.hearthsim.card.Card;
import com.hearthsim.exception.HSInvalidCardException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class to convert a card name into a Card class instance
 *
 */
public class CardFactory {

    private static Map<String, String> cardCache;
    private static Map<String, String> cardIdCache;

    private static void loadCardData() {
        JSONArray implementedCards;
        try {
            InputStream stream = CardFactory.class.getClassLoader().getResourceAsStream("implemented_cards.json");
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            implementedCards = new JSONArray(responseStrBuilder.toString());
        } catch (IOException e) {
            implementedCards = null;
        }
        if (implementedCards != null) {
            cardCache = new HashMap<>();
            cardIdCache = new HashMap<>();
            synchronized (cardCache) {
                for (int indx = 0; indx < implementedCards.length(); ++indx) {
                    JSONObject obj = implementedCards.getJSONObject(indx);
                    String[] cardName = obj.optString("class").split("\\.");
                    cardCache.put(cardName[cardName.length - 1].trim(), obj.optString("class"));
                    cardIdCache.put(obj.getString("id"), obj.optString("class"));
                }
            }
        }
    }

    public static Card getCard(String cardName) throws HSInvalidCardException {
        if (cardCache == null)
            loadCardData();
        String cleanedString = cardName.trim();
        if (cardCache == null)
            throw new HSInvalidCardException("Unable to open implemented_cards.json");

        try {
            Class<?> clazz = Class.forName(cardCache.get(cleanedString));
            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance();
            return (Card)object;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new HSInvalidCardException("Unknown card: " + cleanedString);
        }
    }

    public static Card getCardById(String cardId) throws HSInvalidCardException {
        if (cardIdCache == null)
            loadCardData();
        String cleanedString = cardId.trim();
        if (cardIdCache == null)
            throw new HSInvalidCardException("Unable to open implemented_cards.json");

        try {
            Class<?> clazz = Class.forName(cardIdCache.get(cleanedString));
            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance();
            return (Card)object;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new HSInvalidCardException("Unknown card: " + cleanedString);
        }
    }

}
