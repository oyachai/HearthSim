package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Demonfire extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public Demonfire(boolean hasBeenUsed) {
		super((byte)2, hasBeenUsed);
		
		this.canTargetEnemyHero = false;
		this.canTargetOwnHero = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public Demonfire() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 2 damage to a minion.  If it's a friendly Demon, give it +2/+2 instead.
	 * 
     * @param side
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck for player 0
     * @param deckPlayer1 The deck for player 1
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			if (isCurrentPlayer(side) && targetMinion.getTribe() == MinionTribe.DEMON) {
				targetMinion.setAttack((byte)(targetMinion.getAttack() + 2));
				targetMinion.setHealth((byte)(targetMinion.getHealth() + 2));
			} else {
				toRet = targetMinion.takeDamage((byte)2, PlayerSide.CURRENT_PLAYER, side, boardState, deckPlayer0, deckPlayer1, true, false);
			}
		}
		return toRet;
	}
}
