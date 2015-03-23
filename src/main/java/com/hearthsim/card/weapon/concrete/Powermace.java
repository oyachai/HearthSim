package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.EffectMinionBuff;
import com.hearthsim.event.MinionFilterUntargetedDeathrattle;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;

public class Powermace extends WeaponCard {
    private final static EffectMinionAction<WeaponCard> powermaceEffect = new EffectMinionBuff<>(2, 2);

    private final static MinionFilterUntargetedDeathrattle filter = new MinionFilterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() { return true; }

        @Override
        protected Minion.MinionTribe tribeFilter() { return Minion.MinionTribe.MECH; }
    };

    public Powermace() {
        super();
        this.deathrattleAction_ = new DeathrattleEffectRandomMinion<>(Powermace.powermaceEffect, Powermace.filter);
    }
}
