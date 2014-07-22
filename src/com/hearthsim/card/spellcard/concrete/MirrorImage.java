package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.MirrorImageMinion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class MirrorImage extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public MirrorImage(boolean hasBeenUsed) {
		super("Mirror Image", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public MirrorImage() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new MirrorImage(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Summons either Huffer, Leokk, or Misha
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
			Deck deckPlayer0, Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (minionIndex > 0 || playerIndex == 1) {
			return null;
		}
		
		int numMinions = boardState.data_.getNumMinions_p0();
		if (numMinions >= 7)
			return null;

		Minion mi0 = new MirrorImageMinion();
		boardState.data_.placeCard_hand_p0(mi0);
		HearthTreeNode toRet = mi0.useOn(boardState.data_.getNumCards_hand() - 1, playerIndex, numMinions + 1, boardState, deckPlayer0, deckPlayer1);
		
		if (numMinions < 6) {
			Minion mi1 = new MirrorImageMinion();
			boardState.data_.placeCard_hand_p0(mi1);
			toRet = mi1.useOn(boardState.data_.getNumCards_hand() - 1, playerIndex, numMinions + 1, boardState, deckPlayer0, deckPlayer1);
		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
	}

}
