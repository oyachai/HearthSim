package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Hound;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class UnleashTheHounds extends SpellCard {

	public UnleashTheHounds(boolean hasBeenUsed) {
		super((byte)3, hasBeenUsed);

		this.canTargetEnemyHero = false;
		this.canTargetEnemyMinions = false;
		this.canTargetOwnMinions = false;
	}

	public UnleashTheHounds() {
		this(false);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
     * @param side
     * @param targetMinion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0
     * @param deckPlayer1
     * @param singleRealizationOnly
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
			int numHoundsToSummon = PlayerSide.WAITING_PLAYER.getPlayer(toRet).getNumMinions();
			if (numHoundsToSummon + PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions() > 7)
				numHoundsToSummon = 7 - PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions();
			for (int indx = 0; indx < numHoundsToSummon; ++indx) {
				Minion placementTarget = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions() > 0 ? PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions().getLast() : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getHero();
				toRet = new Hound().summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
			}
		}
		return toRet;
	}

}
