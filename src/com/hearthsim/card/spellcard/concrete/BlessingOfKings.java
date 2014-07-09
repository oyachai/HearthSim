package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class BlessingOfKings extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public BlessingOfKings(boolean hasBeenUsed) {
		super("Blessing of Kings", (byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public BlessingOfKings() {
		this(false);
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
	protected HearthTreeNode<BoardState> use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		if (minionIndex == 0) {
			return null;
		}
		
		boardState.data_.getMinion(playerIndex, minionIndex - 1).setAttack((byte)(boardState.data_.getMinion(playerIndex, minionIndex - 1).getAttack() + 4));
		boardState.data_.getMinion(playerIndex, minionIndex - 1).setHealth((byte)(boardState.data_.getMinion(playerIndex, minionIndex - 1).getHealth() + 4));
		boardState.data_.getMinion(playerIndex, minionIndex - 1).setMaxHealth((byte)(boardState.data_.getMinion(playerIndex, minionIndex - 1).getMaxHealth() + 4));
		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
