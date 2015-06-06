package com.hearthsim.test.util;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.*;

public class DeckFactoryTest 
{
    private ArrayList<ImplementedCard> referenceCards;
    
    @Before
    public void setUp() throws Exception 
    {
        referenceCards = ImplementedCardList.getInstance().getCardList();
    }

    @Test
    public void checkAllClasses() 
    {
        
    }

}
