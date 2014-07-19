package com.hearthsim.card.spellcard.concrete;

import java.util.Iterator;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class ArcaneMissiles extends SpellCard {
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public ArcaneMissiles(boolean hasBeenUsed) {
		super("Arcane Missiles", (byte)1, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public ArcaneMissiles() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new ArcaneMissiles(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 1 damage to three random enemy characters.  The characters can repeat.
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
		if (playerIndex == 0) {
			return null;
		}
		
		if (minionIndex > 0) {
			return null;
		}
		
		int numMissiles = 3 + boardState.data_.getSpellDamage(0);
		
		int numTargets = boardState.data_.getNumMinions_p1() + 1;
		int index = 0;
		while(index < numMissiles) {
			int targetIndex = (int)(numTargets * Math.random());
			if (targetIndex == 0 && boardState.data_.getHero_p1().getHealth() > 0) {
				boardState.data_.getHero_p1().takeDamage((byte)1, 0, 1, targetIndex, boardState, deck);
				++index;
			} else if (targetIndex > 0 && boardState.data_.getMinion_p1(targetIndex-1).getHealth() > 0) {
				boardState.data_.getMinion_p1(targetIndex-1).takeDamage((byte)1, 0, 1, targetIndex, boardState, deck);
				++index;
			}
			if (boardState.data_.getHero_p1().getHealth() <= 0) {
				break;
			}
		}

		Iterator<Minion> iter = boardState.data_.getMinions_p1().iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
			}
		}

		return super.use_core(thisCardIndex, playerIndex, minionIndex, boardState, deck);
	}
}
