package com.hearthsim.test.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

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
    		
    	}
    }

}
