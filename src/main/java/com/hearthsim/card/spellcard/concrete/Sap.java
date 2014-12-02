package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
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
		super((byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Sap() {
		this(false);
	}

	
	public Object deepCopy() {
		return new Sap(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Return an enemy minion to its hand
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	
	protected HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isHero(targetMinion) || isCurrentPlayer(side)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			if (boardState.data_.getNumCardsHandWaitingPlayer() < 10) {
				try {
					Class<?> clazz = Class.forName(targetMinion.getClass().getName());
					Constructor<?> ctor = clazz.getConstructor();
					Object object = ctor.newInstance();
					toRet.data_.placeCardHandWaitingPlayer((Card) object);
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
			toRet.data_.removeMinion(targetMinion);
		}
		return toRet;
	}
}
