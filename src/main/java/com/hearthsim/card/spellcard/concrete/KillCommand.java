package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class KillCommand extends SpellDamage {

	public KillCommand() {
		this(false);
	}

	public KillCommand(boolean hasBeenUsed) {
		super("Kill Command", (byte)3, (byte)3, hasBeenUsed);
	}

	@Override
	public Object deepCopy() {
		return new KillCommand(this.hasBeenUsed_);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 3 damage.  If you have a beast, deals 5 damage.
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
		boolean haveBeast = false;
		for (final Minion minion : boardState.data_.getCurrentPlayer().getMinions()) {
			haveBeast = haveBeast || minion instanceof Beast;
		}
		if (haveBeast)
			this.damage_ = (byte)5;
		else
			this.damage_ = (byte)3;
		HearthTreeNode toRet = super.use_core(playerModel, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

		return toRet;
	}
}
