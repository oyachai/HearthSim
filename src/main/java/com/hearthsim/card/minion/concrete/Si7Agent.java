package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

/**
 * Created by oyachai on 2/1/15.
 */
public class Si7Agent extends Minion implements MinionTargetableBattlecry {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public Si7Agent() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
    }

    @Override
    public boolean hasBattlecry() {
        return false;
    }

        @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.FRIENDLY_HERO, BattlecryTargetType.ENEMY_HERO, BattlecryTargetType.FRIENDLY_MINIONS, BattlecryTargetType.ENEMY_MINIONS);
    }

    /**
     * Battlecry: Deal 1 damage to a chosen target
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = boardState;
        if (toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER).isComboEnabled()) {
            toRet = targetMinion.takeDamage((byte) 2, PlayerSide.CURRENT_PLAYER, side, toRet, false, true);
            return toRet;
        } else {
            return null;
        }
    }

}
