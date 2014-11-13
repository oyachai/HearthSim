package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HolyFire extends SpellDamage {


	public HolyFire() {
		this(false);
	}

	public HolyFire(boolean hasBeenUsed) {
		super((byte)6, (byte)5, hasBeenUsed);
		
		this.canTargetOwnHero = false; // TODO card as printed allows this
	}

	@Override
	public SpellDamage deepCopy() {
		return new HolyFire(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 5 damage and heals the hero for 5.
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
			toRet.data_.getCurrentPlayerHero().takeHeal((byte)5, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
		}
		return toRet;
	}
}
