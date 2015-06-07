package com.hearthsim.test.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.*;
import com.hearthsim.util.DeckFactory;
import com.hearthsim.util.DeckFactory.DeckFactoryBuilder;

public class DeckFactoryTest 
{
    private ArrayList<ImplementedCard> referenceCards;
    private HashSet<String> allHeroes;
    
    @Before
    public void setUp() throws Exception 
    {
        referenceCards = ImplementedCardList.getInstance().getCardList();
        allHeroes = new HashSet<String>();
        for(ImplementedCard card : referenceCards)
        	allHeroes.add(card.charClass_);
    }

    @Test
    public void checkOnlySelectedClass() 
    {
    	for(String hero : allHeroes)
    	{
    		DeckFactoryBuilder filterByClass = new DeckFactoryBuilder();
    		filterByClass.filterByHero(hero, "neutral");
    		ArrayList<ImplementedCard> cardsFromFactory = filterByClass.buildDeckFactory().getAllPossibleCards();
    		for(ImplementedCard card : cardsFromFactory)
    		{
    			String errorMessage = card.charClass_ + " is not neutral or " + hero;
    			assertTrue(errorMessage, card.charClass_.equals(hero) || card.charClass_.equals("neutral"));
    		}
    	}
    }
    
    @Test
    public void checkHasAllOfClass()
    {
    	for(String hero : allHeroes)
    	{
    		DeckFactoryBuilder filterByClass = new DeckFactoryBuilder();
    		filterByClass.filterByHero(hero, "neutral");
    		ArrayList<ImplementedCard> cardsFromFactory = filterByClass.buildDeckFactory().getAllPossibleCards();
    		HashSet<ImplementedCard> cardsOfDeckFactory = new HashSet<ImplementedCard>();
    		cardsOfDeckFactory.addAll(cardsFromFactory);
    		
    		ArrayList<ImplementedCard> cardsOfHeroList = new ArrayList<ImplementedCard>(referenceCards);
    		cardsOfHeroList.removeIf((card) -> !card.charClass_.equals(hero) || !card.charClass_.equals("neutral") || !card.collectible);
    		
    		for(ImplementedCard card : cardsOfHeroList)
    		{
    			String errorMessage = "DeckFactory of " + hero + " did not contain " + card.name_;
    			assertTrue(errorMessage, cardsOfDeckFactory.contains(card));
    		}
    	}
    }
}
