package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.BattlecryTargetType;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class LeeroyJenkins extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public LeeroyJenkins() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}

	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_TARGET);
	}
	
	/**
	 * Battlecry: summon two 1/1 whelps for your opponent
	 */
	@Override
	public HearthTreeNode useUntargetableBattlecry_core(
			Minion minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		for (int index = 0; index < 2; ++index) {
        	int numMinions = PlayerSide.WAITING_PLAYER.getPlayer(toRet).getNumMinions();
        	if (numMinions < 7) {
            	Minion newMinion = new Whelp();
            	Minion placementTarget = toRet.data_.getCharacter(PlayerSide.WAITING_PLAYER, numMinions);
				toRet = newMinion.summonMinion(PlayerSide.WAITING_PLAYER, placementTarget, toRet, deckPlayer0, deckPlayer1, false, true);
        	}
		}
		return toRet;
	}

	
}
