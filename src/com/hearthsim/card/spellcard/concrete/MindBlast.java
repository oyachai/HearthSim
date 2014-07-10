package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class MindBlast extends SpellDamage {

	public MindBlast() {
		this(false);
	}

	public MindBlast(boolean hasBeenUsed) {
		super("Mind Blast", (byte)2, (byte)5, hasBeenUsed);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 5 damage to enemy hero
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
		if (playerIndex == 0 || minionIndex > 0) 
			return null;
		
		HearthTreeNode<BoardState> toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
		return toRet;
	}	
}
