package com.hearthsim.card.spellcard.concrete;

import java.util.Iterator;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.util.BoardState;

public class ArcaneExplosion extends SpellCard {
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public ArcaneExplosion(boolean hasBeenUsed) {
		super("Arcane Explosion", (byte)0, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public ArcaneExplosion() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This card heals the target minion up to full health and gives it taunt.  Cannot be used on heroes.
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public BoardState useOn(int thisCardIndex, int playerIndex, int minionIndex, BoardState boardState) {
		if (playerIndex == 0) {
			return null;
		}
		
		if (minionIndex > 0) {
			return null;
		}
		
		Iterator<Minion> iter = boardState.getMinions_p1().iterator();
		while (iter.hasNext()) {
			Minion targetMinion = iter.next();
			targetMinion.setHealth((byte)(targetMinion.getHealth() - 1));
			if (targetMinion.getHealth() <= 0) {
				iter.remove();
			}
		}

		boardState.removeCard_hand(thisCardIndex);
		return boardState;
	}
}
