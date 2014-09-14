package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShadowBolt extends SpellDamage {

	public ShadowBolt() {
		this(false);
	}
	
	public ShadowBolt(boolean hasBeenUsed) {
		super("Shadow Bolt", (byte)3, (byte)4, hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new ShadowBolt(this.hasBeenUsed_);
	}


	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Cannot be used on the heroes
	 * 
	 *
     * @param playerModel
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerModel playerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (targetMinion instanceof Hero) 
			return null;
		
		return super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
	}
}
