package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
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
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (targetMinion instanceof Hero) 
			return null;
		
		return super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
	}
}
