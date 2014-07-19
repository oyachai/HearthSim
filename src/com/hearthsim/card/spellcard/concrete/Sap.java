package com.hearthsim.card.spellcard.concrete;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

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
	 * Gives a minion +4/+4
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
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (minionIndex == 0 || playerIndex == 0) {
			return null;
		}
		
		Minion targetMinion = boardState.data_.getMinion_p1(minionIndex - 1);
		targetMinion.silenced(playerIndex, minionIndex, boardState, deck);
		if (boardState.data_.getNumCards_hand_p1() < 10) {
			try {
				Class<?> clazz = Class.forName(targetMinion.getClass().getName());
				Constructor<?> ctor = clazz.getConstructor();
				Object object = ctor.newInstance();
				boardState.data_.placeCard_hand_p1((Card)object);
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
		boardState.data_.removeMinion_p1(minionIndex - 1);
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
