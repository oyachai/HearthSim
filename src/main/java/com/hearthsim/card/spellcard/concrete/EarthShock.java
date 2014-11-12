package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EarthShock extends SpellDamage {
	
	public EarthShock() {
		this(false);
	}

	public EarthShock(boolean hasBeenUsed) {
		super((byte)1, (byte)1, hasBeenUsed);
	}

	@Override
	public SpellDamage deepCopy() {
		return new EarthShock(this.hasBeenUsed);
	}
	
	
	@Override
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		if(!super.canBeUsedOn(playerSide, minion, boardModel)) {
			return false;
		}

		if (isHero(minion)) {
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Silence a minion, then deal 1 damage
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
		targetMinion.silenced(side, boardState);
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		return toRet;
	}
}
