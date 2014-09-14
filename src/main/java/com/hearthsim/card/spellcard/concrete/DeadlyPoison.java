package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeadlyPoison extends SpellCard {

	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public DeadlyPoison(boolean hasBeenUsed) {
		super("Deadly Poison", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public DeadlyPoison() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new DeadlyPoison(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Give your weapon +2 attack
	 * 
	 *
     * @param playerModel
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerModel playerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (boardState.data_.getWaitingPlayer() == playerModel || !(targetMinion instanceof Hero)) {
			return null;
		}
		Hero hero = (Hero)targetMinion;
		if (hero.getWeaponCharge() == 0)
			return null;

		HearthTreeNode toRet = super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			hero.setAttack((byte)(hero.getAttack() + 2));
		}
		return toRet;
	}
	
}
