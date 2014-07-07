package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class HammerOfWrath extends SpellDamage {
	
	public HammerOfWrath() {
		this(false);
	}

	public HammerOfWrath(boolean hasBeenUsed) {
		super("Hammer Of Wrath", (byte)4, (byte)3, hasBeenUsed);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 1 damage and freezes an enemy
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

		HearthTreeNode<BoardState> toRet = super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);

		if (toRet != null) {
			toRet.data_.placeCard_hand_p0(deck.drawCard(toRet.data_.getDeckPos_p0()));
			toRet.data_.setDeckPos_p0(toRet.data_.getDeckPos_p0() + 1);
		}

		return toRet;
	}
}
