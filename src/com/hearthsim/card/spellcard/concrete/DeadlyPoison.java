package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

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
	
	/**
	 * 
	 * Use the weapon card
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode<BoardState> use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (this.hasBeenUsed()) {
			//Card is already used, nothing to do
			return null;
		}
				
		if (playerIndex == 1 || minionIndex > 0) {
			return null;
		}

		
		HearthTreeNode<BoardState> toRet = boardState;
		if (toRet != null) {
			if (toRet.data_.getHero_p0().getWeaponCharge() > 0) {
				toRet.data_.getHero_p0().setAttack((byte)(toRet.data_.getHero_p0().getAttack() + 2));
			} else {
				return null;
			}
			this.hasBeenUsed(true);
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, toRet, deck);
	}
	
}
