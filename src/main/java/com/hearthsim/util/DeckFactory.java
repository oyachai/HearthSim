package com.hearthsim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.ImplementedCardList.ImplementedCard;

public class DeckFactory
{
	private ArrayList<ImplementedCard> cards;
	private boolean limitCopies;
	private Random gen;

	protected DeckFactory(Predicate<ImplementedCard> filter,
			boolean limitCopies)
	{
		cards = ImplementedCardList.getInstance().getCardList();
		cards.removeIf(filter);
		gen = new Random();
	}

	public Deck generateRandomDeck()
	{
		Card[] result = new Card[30];
		if (limitCopies)
		{
			HashMap<ImplementedCard, Integer> cardsInDeck = new HashMap<ImplementedCard, Integer>();
			for (int i = 0; i < 30; i++)
			{
				ImplementedCard toAdd;
				// Keep going until a card is found that can be added to the
				// deck.
				while (true)
				{
					toAdd = cards.get(gen.nextInt(cards.size()));
					if (!cardsInDeck.containsKey(toAdd))
					{
						cardsInDeck.put(toAdd, 1);
						break;
					}
					else if (cardsInDeck.get(toAdd).equals(1)
							&& !toAdd.rarity_.equals("legendary"))
					{
						cardsInDeck.put(toAdd, 2);
						break;
					}
				}
				result[i] = toAdd.createCardInstance();
			}
		}
		else
		{
			for (int i = 0; i < 30; i++)
			{
				result[i] = cards.get(gen.nextInt(cards.size()))
						.createCardInstance();
			}
		}

		return new Deck(result);
	}

	public static class DeckFactoryBuilder
	{
		private Predicate<ImplementedCard> filter;
		private boolean limitCopies;
		private boolean allowUncollectible;

		public DeckFactoryBuilder()
		{
			filter = (card) -> false;
			limitCopies = true;
			allowUncollectible = false;
		}

		public void filterByRarity(String rarity)
		{
			filter.or((card) -> card.rarity_ != rarity);
		}

		public void filterByHero(String hero)
		{
			filter.or((card) -> card.charClass_ != hero);
		}

		public void allowUncollectible()
		{
			allowUncollectible = true;
		}

		public DeckFactory createDeckFactory()
		{
			if (!allowUncollectible) filter.or((card) -> !card.collectible);
			return new DeckFactory(filter, limitCopies);
		}
		
		public void filterByManaCost(int minimumCost, int maximumCost)
		{
			filter.or((card) -> card.mana_ < minimumCost || card.mana_ > maximumCost);
		}

		public void allowUnlimitedCopiesOfCards()
		{
			limitCopies = false;
		}
	}
}
