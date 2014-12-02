package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class KillCommand extends SpellDamage {

	public KillCommand() {
		this(false);
	}

	public KillCommand(boolean hasBeenUsed) {
		super((byte)3, (byte)3, hasBeenUsed);
	}

	
	public Object deepCopy() {
		return new KillCommand(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Deals 3 damage.  If you have a beast, deals 5 damage.
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
		boolean haveBeast = false;
		for (final BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getMinions()) {
			haveBeast = haveBeast || minion instanceof Beast;
		}
		if (haveBeast)
			this.damage_ = (byte)5;
		else
			this.damage_ = (byte)3;
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

		return toRet;
	}
}
