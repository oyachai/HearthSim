package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostElemental extends Minion implements MinionTargetableBattlecry {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public FrostElemental() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
    }

    /**
     * Let's assume that it is never a good idea to freeze your own character
     */
	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.ENEMY_HERO, BattlecryTargetType.ENEMY_MINIONS);
	}
	
	/**
	 * Battlecry: Freeze a character
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
		targetMinion.setFrozen(true);
		return boardState;
	}

}
