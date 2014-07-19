package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShadowWordPain extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public ShadowWordPain(boolean hasBeenUsed) {
		super("Shadow Word: Pain", (byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public ShadowWordPain() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new ShadowWordPain(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives all friendly characters +2 attack for this turn
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
		if (minionIndex == 0) {
			return null;
		}
		
		Minion targetMinion = boardState.data_.getMinion(playerIndex, minionIndex - 1);
		if (targetMinion.getAttack() + targetMinion.getExtraAttackUntilTurnEnd() > 3)
			return null;
		
		targetMinion.destroyed(playerIndex, minionIndex, boardState, deck);
		boardState.data_.removeMinion(playerIndex, minionIndex - 1);
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
