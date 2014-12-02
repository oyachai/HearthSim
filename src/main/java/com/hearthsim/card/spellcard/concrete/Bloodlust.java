package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Bloodlust extends SpellCard {

	public Bloodlust() {
		this(false);
	}

	public Bloodlust(boolean hasBeenUsed) {
		super((byte)5, hasBeenUsed);
	}
	
	
	public Object deepCopy() {
		return new Bloodlust(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Give your minions +3 attack for this turn
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	
	protected HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isWaitingPlayer(side) || isNotHero(targetMinion)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		
		for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			minion.setExtraAttackUntilTurnEnd((byte)3);
		}
		return toRet;
	}
}
