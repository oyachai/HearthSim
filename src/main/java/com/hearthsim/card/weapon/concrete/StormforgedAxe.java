package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class StormforgedAxe extends WeaponCard {

	public StormforgedAxe() {
		this(false);
	}

	public StormforgedAxe(boolean hasBeenUsed) {
		super("Stormforged Axe", (byte)2, (byte)2, (byte)3, hasBeenUsed);
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Overload (1)
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
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			toRet.data_.addOverload(side, (byte)1);
		}
		return toRet;
	}
}
