package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class Spellbreaker extends Minion implements MinionTargetableBattlecry {

    public Spellbreaker() {
        super();
    }

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
    }

    /**
     * Battlecry: Silence a minion
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = boardState;
        targetMinion.silenced(PlayerSide.CURRENT_PLAYER, toRet.data_);
        return toRet;
    }

}
