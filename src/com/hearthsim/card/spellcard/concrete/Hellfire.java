package com.hearthsim.card.spellcard.concrete;

import java.util.Iterator;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;

public class Hellfire extends SpellCard {

	private static final byte DAMAGE_AMOUNT = 3;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Hellfire(boolean hasBeenUsed) {
		super("Hellfire", (byte)4, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Hellfire() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new Hellfire(this.hasBeenUsed_);
	}

	/**
	 * 
	 * Hellfire
	 * 
	 * Deals 3 damage to all characters
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
		if (playerIndex > 0 || minionIndex > 0) 
			return null;
		
		HearthTreeNode<BoardState> toRet = boardState;

		boardState.data_.getHero_p1().takeDamage(DAMAGE_AMOUNT, 0, 0, 0, boardState, deck, true);
		for (int indx = 0; indx < boardState.data_.getNumMinions_p1(); ++indx) {
			Minion targetMinion = boardState.data_.getMinion_p1(indx);
			targetMinion.takeDamage(DAMAGE_AMOUNT, 0, 1, indx + 1, boardState, deck, true);
		}

		boardState.data_.getHero_p0().takeDamage(DAMAGE_AMOUNT, 0, 0, 0, boardState, deck, true);
		for (int indx = 0; indx < boardState.data_.getNumMinions_p0(); ++indx) {
			Minion targetMinion = boardState.data_.getMinion_p0(indx);
			targetMinion.takeDamage(DAMAGE_AMOUNT, 0, 0, indx + 1, boardState, deck, true);
		}
		
		Iterator<Minion> iter = boardState.data_.getMinions_p0().iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
			}
		}

		iter = boardState.data_.getMinions_p1().iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
			}
		}

		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
