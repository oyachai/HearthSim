package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Frog;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hex extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Hex(boolean hasBeenUsed) {
		super("Hex", (byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Hex() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Hex(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Transform a minion into 0/1 frog with Taunt
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
			Deck deckPlayer1)
		throws HSException
	{
		if (targetMinion instanceof Hero) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			Frog frog = new Frog();
			if (targetPlayerIndex == 0) {
				boardState.data_.placeMinion(0, frog, toRet.data_.getMinions_p0().indexOf(targetMinion));
				boardState.data_.removeMinion_p0(targetMinion);
			} else if (targetPlayerIndex == 1) {
				boardState.data_.placeMinion(1, frog, toRet.data_.getMinions_p1().indexOf(targetMinion));
				boardState.data_.removeMinion_p1(targetMinion);
			} else {
				throw new HSInvalidPlayerIndexException();
			}
		}
		return toRet;
	}

}
