package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionStateFactory;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostNova extends SpellCard {
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	MinionStateFactory mf = new MinionStateFactory();
	public FrostNova(boolean hasBeenUsed) {
		super((byte)3, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public FrostNova() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new Flamestrike(this.hasBeenUsed);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This freeze all enemy minions
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
		if (isCurrentPlayer(side)) {
			return null;
		}
		
		if (isNotHero(targetMinion)) {
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				//minion.setFrozen(true);
				minion.addState(mf.makeFrozen());
			}
		}		
		return toRet;
	}

}
