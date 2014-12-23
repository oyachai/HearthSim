package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;


public class StormpikeCommando extends Minion implements MinionTargetableBattlecry {

	private static final byte BATTLECRY_DAMAGE = 2;

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public StormpikeCommando() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.FRIENDLY_HERO, BattlecryTargetType.ENEMY_HERO, BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
	}
	
	/**
	 * Battlecry: Deal 2 damage to a chosen target
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
		toRet = targetMinion.takeDamage(BATTLECRY_DAMAGE, PlayerSide.CURRENT_PLAYER, side, toRet, deckPlayer0, deckPlayer1, false, false);
		return toRet;
	}

}
