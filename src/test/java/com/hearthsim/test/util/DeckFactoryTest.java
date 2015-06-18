package com.hearthsim.test.util;

import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.ImplementedCard;
import com.hearthsim.util.DeckFactory;
import com.hearthsim.util.DeckFactory.DeckFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.*;

public class DeckFactoryTest {
    private ArrayList<ImplementedCard> referenceCards;
    private HashSet<String> allHeroes;

    @Before
    public void setUp() throws Exception {
        referenceCards = ImplementedCardList.getInstance().getCardList();
        allHeroes = new HashSet<String>();
        for (ImplementedCard card : referenceCards)
            allHeroes.add(card.charClass_);
    }

    @Test
    public void checkOnlySelectedClass() {
        for (String hero : allHeroes) {
            DeckFactoryBuilder filterByClass = new DeckFactoryBuilder();
            filterByClass.filterByHero(hero, "neutral");
            ArrayList<ImplementedCard> cardsFromFactory = filterByClass
                    .buildDeckFactory().getAllPossibleCards();
            for (ImplementedCard card : cardsFromFactory) {
                String errorMessage = card.charClass_ + " is not neutral or "
                        + hero;
                assertTrue(errorMessage, card.charClass_.equals(hero)
                        || card.charClass_.equals("neutral"));
            }
        }
    }

    @Test
    public void checkHasAllOfClass() {
        for (String hero : allHeroes) {
            DeckFactoryBuilder filterByClass = new DeckFactoryBuilder();
            filterByClass.filterByHero(hero, "neutral");
            ArrayList<ImplementedCard> cardsFromFactory = filterByClass
                    .buildDeckFactory().getAllPossibleCards();
            HashSet<ImplementedCard> cardsOfDeckFactory = new HashSet<ImplementedCard>();
            cardsOfDeckFactory.addAll(cardsFromFactory);

            ArrayList<ImplementedCard> cardsOfHeroList = new ArrayList<ImplementedCard>(
                    referenceCards);
            cardsOfHeroList.removeIf((card) -> !card.charClass_.equals(hero)
                    || !card.charClass_.equals("neutral") || !card.collectible);

            for (ImplementedCard card : cardsOfHeroList) {
                String errorMessage = "DeckFactory of " + hero
                        + " did not contain " + card.name_;
                assertTrue(errorMessage, cardsOfDeckFactory.contains(card));
            }
        }
    }

    @Test
    public void checkHasOnlyCollectible() {
        ArrayList<ImplementedCard> onlyCollectible = new DeckFactoryBuilder()
        .buildDeckFactory().getAllPossibleCards();

        for (ImplementedCard card : onlyCollectible) {
            String errorMessage = card.name_ + " is not collectible.";
            assertTrue(errorMessage, card.collectible);
        }
    }

    @Test
    public void checkAllowUnCollectibleHasEveryCard() {
        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.allowUncollectible();
        ArrayList<ImplementedCard> allCardsList = builder.buildDeckFactory()
                .getAllPossibleCards();
        HashSet<ImplementedCard> allCards = new HashSet<ImplementedCard>();
        allCards.addAll(allCardsList);

        for (ImplementedCard card : referenceCards) {
            String errorMessage = "DeckFactory did not contain " + card.name_;
            assertTrue(errorMessage, allCards.contains(card));
        }
    }

    @Test
    public void checkFilterByManaCost() {
        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.filterByManaCost(3, 7);
        ArrayList<ImplementedCard> filteredCards = builder.buildDeckFactory()
                .getAllPossibleCards();

        for (ImplementedCard card : filteredCards)
            assertTrue(card.mana_ >= 3 && card.mana_ <= 7);
    }

    @Test
    public void checkLimitedCopies() {
        Deck deckTest = new DeckFactoryBuilder().buildDeckFactory()
                .generateRandomDeck();
        HashMap<String, Integer> cardCounts = new HashMap<String, Integer>();

        HashMap<String, ImplementedCard> nameToCard = new HashMap<String, ImplementedCard>();
        for (ImplementedCard card : referenceCards)
            nameToCard.put(card.name_, card);
        for (int i = 0; i < 30; i++) {
            String cardName = deckTest.drawCard(i).getName();
            if (cardCounts.containsKey(cardName)) {
                if (nameToCard.get(cardName).rarity_.equals("legendary"))
                    fail("Should only have one copy of " + cardName + ".");
                else {
                    assertEquals(cardCounts.get(cardName), new Integer(1));
                    cardCounts.put(cardName, 2);
                }
            } else
                cardCounts.put(cardName, 1);
        }
    }

    @Test
    public void checkUnlimitedCopiesLegendary() {
        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.allowUnlimitedCopiesOfCards();
        DeckFactory factory = builder.buildDeckFactory();
        long start = System.currentTimeMillis();
        boolean testPassed = false;

        // This maximum time limit is extremely generous and should take well
        // under a second to complete.
        mainloop: while (System.currentTimeMillis() - start < 10_000) {
            Deck deckTest = factory.generateRandomDeck();
            HashMap<String, Integer> cardCounts = new HashMap<String, Integer>();
            HashMap<String, ImplementedCard> nameToCard = new HashMap<String, ImplementedCard>();
            for (ImplementedCard card : referenceCards)
                nameToCard.put(card.name_, card);
            for (int i = 0; i < 30; i++) {
                String cardName = deckTest.drawCard(i).getName();
                if (cardCounts.containsKey(cardName)) {
                    if (nameToCard.get(cardName).rarity_.equals("legendary")) {
                        testPassed = true;
                        break mainloop;
                    } else {
                        cardCounts.put(cardName, cardCounts.get(cardName) + 1);
                    }
                } else
                    cardCounts.put(cardName, 1);
            }
        }

        assertTrue(
                "DeckFactory could not generate a deck with more than one legendary within the time limit.",
                testPassed);
    }

    @Test
    public void checkUnlimitedCopiesNonLegendary() {
        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.allowUnlimitedCopiesOfCards();
        DeckFactory factory = builder.buildDeckFactory();
        long start = System.currentTimeMillis();
        boolean testPassed = false;

        // This maximum time limit is extremely generous and should take well
        // under a second to complete.
        mainloop: while (System.currentTimeMillis() - start < 10_000) {
            Deck deckTest = factory.generateRandomDeck();
            HashMap<String, Integer> cardCounts = new HashMap<String, Integer>();
            HashMap<String, ImplementedCard> nameToCard = new HashMap<String, ImplementedCard>();
            for (ImplementedCard card : referenceCards)
                nameToCard.put(card.name_, card);
            for (int i = 0; i < 30; i++) {
                String cardName = deckTest.drawCard(i).getName();
                if (cardCounts.containsKey(cardName)) {
                    if (nameToCard.get(cardName).rarity_.equals("legendary")) {
                        cardCounts.put(cardName, cardCounts.get(cardName) + 1);
                    } else {
                        cardCounts.put(cardName, cardCounts.get(cardName) + 1);
                        if (cardCounts.get(cardName) > 2) {
                            testPassed = true;
                            break mainloop;
                        }
                    }
                } else
                    cardCounts.put(cardName, 1);
            }
        }

        assertTrue(
                "DeckFactory could not generate a deck with more than one legendary within the time limit.",
                testPassed);
    }

    @Test
    public void checkIncludeSpecifiedCardsLimitedCopies() {
        Random gen = new Random();
        ImplementedCard card1, card2;
        card1 = referenceCards.get(gen.nextInt(referenceCards.size()));
        card2 = referenceCards.get(gen.nextInt(referenceCards.size()));

        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.includeSpecificCards(card1, card2);
        Deck testDeck = builder.buildDeckFactory().generateRandomDeck();

        boolean test1Passed = false;
        boolean test2Passed = false;
        for (int i = 0; i < 30; i++) {
            if (testDeck.drawCard(i).getName().equals(card1.name_))
                test1Passed = true;
            if (testDeck.drawCard(i).getName().equals(card2.name_))
                test2Passed = true;
        }

        assertTrue(card1.name_, test1Passed);
        assertTrue(card2.name_, test2Passed);
    }

    @Test
    public void checkIncludeSpecifiedCardsUnlimitedCopies() {
        Random gen = new Random();
        ImplementedCard card1, card2;
        card1 = referenceCards.get(gen.nextInt(referenceCards.size()));
        card2 = referenceCards.get(gen.nextInt(referenceCards.size()));

        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.includeSpecificCards(card1, card2);
        builder.allowUnlimitedCopiesOfCards();
        Deck testDeck = builder.buildDeckFactory().generateRandomDeck();

        boolean test1Passed = false;
        boolean test2Passed = false;
        for (int i = 0; i < 30; i++) {
            if (testDeck.drawCard(i).getName().equals(card1.name_))
                test1Passed = true;
            if (testDeck.drawCard(i).getName().equals(card2.name_))
                test2Passed = true;
        }

        assertTrue(card1.name_, test1Passed);
        assertTrue(card2.name_, test2Passed);
    }

    @Test
    public void checkIncludeDuplicateSpecificCards() {
        Random gen = new Random();
        ImplementedCard card1, card2;
        card1 = referenceCards.get(gen.nextInt(referenceCards.size()));
        card2 = card1;

        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.includeSpecificCards(card1, card2);
        Deck testDeck = builder.buildDeckFactory().generateRandomDeck();

        int count = 0;
        for (int i = 0; i < 30; i++) {
            if (testDeck.drawCard(i).getName().equals(card1.name_))
                count++;
        }

        assertTrue(card1.name_, count >= 2);
    }

    @Test
    public void checkFilterByRarity() {
        DeckFactoryBuilder builder = new DeckFactoryBuilder();
        builder.filterByRarity("rare", "epic");
        ArrayList<ImplementedCard> allCards = builder.buildDeckFactory()
                .getAllPossibleCards();

        for (ImplementedCard card : allCards)
            assertTrue(card.rarity_.equals("rare")
                    || card.rarity_.equals("epic"));
    }
}
