package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.minion.AuraTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionWithAura;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;


public class RaidLeader extends Minion implements MinionWithAura {

    private static final boolean HERO_TARGETABLE = true;

    public RaidLeader() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

    }

    @Override
    public EnumSet<AuraTargetType> getAuraTargets() {
        return EnumSet.of(AuraTargetType.AURA_FRIENDLY_MINIONS);
    }


    @Override
    public void applyAura(PlayerSide targetSide, Minion targetMinion,
            BoardModel boardModel) {
        targetMinion.setAuraAttack((byte)(targetMinion.getAuraAttack() + 1));
    }


    @Override
    public void removeAura(PlayerSide targetSide, Minion targetMinion,
            BoardModel boardModel) {
        targetMinion.setAuraAttack((byte)(targetMinion.getAuraAttack() - 1));
    }

}
