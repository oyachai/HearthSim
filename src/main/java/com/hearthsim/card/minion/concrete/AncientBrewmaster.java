package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientBrewmaster extends Minion implements MinionTargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public AncientBrewmaster() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS);
	}
	
	/**
	 * Battlecry: Change an enemy minion's attack to 1
	 */
	@Override
	public HearthTreeNode useTargetableBattlecry_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		if (toRet != null) {
			if (boardState.data_.getNumCardsHandCurrentPlayer() < 10) {
				Minion copy = targetMinion.createResetCopy();
				if(copy != null) {
					toRet.data_.placeCardHandCurrentPlayer(copy);
				}
			}
			toRet.data_.removeMinion(targetMinion);
		}
		return toRet;
	}
}
