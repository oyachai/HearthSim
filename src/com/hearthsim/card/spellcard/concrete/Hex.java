package com.hearthsim.card.spellcard.concrete;

import java.util.Iterator;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Frog;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

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
		
		Frog frog = new Frog();
		if (playerIndex == 0) {
			boardState.data_.placeMinion(0, frog, minionIndex);
			boardState.data_.removeMinion_p0(minionIndex - 1);
		} else if (playerIndex == 1) {
			boardState.data_.placeMinion(1, frog, minionIndex);
			boardState.data_.removeMinion_p1(minionIndex - 1);			
		} else {
			throw new HSInvalidPlayerIndexException();
		}

		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}

}
