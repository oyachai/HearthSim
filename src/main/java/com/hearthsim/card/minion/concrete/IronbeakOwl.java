package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class IronbeakOwl extends Minion implements MinionTargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public IronbeakOwl() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
	}
	
	/**
	 * Battlecry: Silence a minion
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
		targetMinion.silenced(PlayerSide.CURRENT_PLAYER, toRet.data_);
		return toRet;
	}

}
