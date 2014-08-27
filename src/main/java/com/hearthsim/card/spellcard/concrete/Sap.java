package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Sap extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Sap(boolean hasBeenUsed) {
		super("Sap", (byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Sap() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new Sap(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Return an enemy minion to its hand
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if ((targetMinion instanceof Hero) || targetPlayerIndex == 0) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			targetMinion.silenced(targetPlayerIndex, toRet, deckPlayer0, deckPlayer1);
			if (boardState.data_.getNumCards_hand_p1() < 10) {
				try {
					Class<?> clazz = Class.forName(targetMinion.getClass().getName());
					Constructor<?> ctor = clazz.getConstructor();
					Object object = ctor.newInstance();
					toRet.data_.placeCard_hand_p1((Card)object);
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			toRet.data_.removeMinion(1, targetMinion);
		}
		return toRet;
	}
}
