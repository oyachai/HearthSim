package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class AcidicSwampOoze extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public AcidicSwampOoze() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_TARGET);
	}
	
	/**
	 * Battlecry: Destroy your opponent's weapon
	 */
	
	public HearthTreeNode useUntargetableBattlecry_core(
			BaseEntity minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		if (toRet.data_.getWaitingPlayerHero().getWeaponCharge() > 0) {
			toRet.data_.getWaitingPlayerHero().setWeaponCharge((byte)0);
			toRet.data_.getWaitingPlayerHero().setAttack((byte)0);
		}
		return toRet;
	}

}
