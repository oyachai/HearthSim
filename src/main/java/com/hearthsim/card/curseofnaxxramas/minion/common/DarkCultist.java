package com.hearthsim.card.curseofnaxxramas.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacterUntargetedDeathrattle;

public class DarkCultist extends Minion {

    private final static EffectCharacter darkCultistEffect = new EffectCharacterBuffDelta(0, 3);

    private final static FilterCharacterUntargetedDeathrattle filter = new FilterCharacterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public DarkCultist() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(DarkCultist.darkCultistEffect, DarkCultist.filter);
    }
}
