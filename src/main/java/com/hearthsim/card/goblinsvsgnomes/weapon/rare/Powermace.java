package com.hearthsim.card.goblinsvsgnomes.weapon.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacterUntargetedDeathrattle;

public class Powermace extends WeaponCard {
    private final static EffectCharacter powermaceEffect = new EffectCharacterBuffDelta(2, 2);

    private final static FilterCharacterUntargetedDeathrattle filter = new FilterCharacterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected Minion.MinionTribe tribeFilter() {
            return Minion.MinionTribe.MECH;
        }
    };

    public Powermace() {
        super();
        this.deathrattleAction_ = new DeathrattleEffectRandomMinion(Powermace.powermaceEffect, Powermace.filter);
    }
}
