package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TotemicMight extends SpellCard {

	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public TotemicMight(boolean hasBeenUsed) {
		super((byte)0, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetEnemyMinions = false;
		this.canTargetOwnMinions = false;
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public TotemicMight() {
		this(false);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Gives all friendly totems +2 health
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
			for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				if (minion.getTribe() == MinionTribe.TOTEM) {
					minion.setHealth((byte)(2 + minion.getHealth()));
					minion.setMaxHealth((byte)(2 + minion.getMaxHealth()));
				}
			}
		}
		return toRet;
	}
}
