package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class Bloodlust extends SpellCard {

	public Bloodlust() {
		this(false);
	}

	public Bloodlust(boolean hasBeenUsed) {
		super("Bloodlust", (byte)5, hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new Bloodlust(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Give your minions +3 attack for this turn
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
		if (boardState.data_.getWaitingPlayer() == playerModel || !(targetMinion instanceof Hero)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		
		for (Minion minion : toRet.data_.getCurrentPlayer().getMinions()) {
			minion.setExtraAttackUntilTurnEnd((byte)3);
		}
		return toRet;
	}
}
