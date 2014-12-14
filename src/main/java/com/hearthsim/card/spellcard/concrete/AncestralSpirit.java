package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.deathrattle.DeathrattleSummonMinionAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncestralSpirit extends SpellCard {
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public AncestralSpirit(boolean hasBeenUsed) {
		super((byte)2, hasBeenUsed);
		
		this.canTargetEnemyHero = false;
		this.canTargetOwnHero = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public AncestralSpirit() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Give a minion 'Deathrattle: Resummon this minion'
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
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
			targetMinion.setDeathrattle(new DeathrattleSummonMinionAction(targetMinion.getClass(), 1));
		}
		return toRet;
	}
}
