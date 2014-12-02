package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShadowBolt extends SpellDamage {

	public ShadowBolt() {
		this(false);
	}
	
	public ShadowBolt(boolean hasBeenUsed) {
		super((byte)3, (byte)4, hasBeenUsed);
	}
	
	
	public Object deepCopy() {
		return new ShadowBolt(this.hasBeenUsed);
	}


	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Cannot be used on the heroes
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
		if (isHero(targetMinion))
			return null;
		
		return super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
	}
}
