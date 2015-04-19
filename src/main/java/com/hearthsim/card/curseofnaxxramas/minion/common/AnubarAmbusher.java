package com.hearthsim.card.curseofnaxxramas.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacterUntargetedDeathrattle;

public class AnubarAmbusher extends Minion {

    private final static EffectCharacter effect = EffectCharacter.BOUNCE;

    private final static FilterCharacterUntargetedDeathrattle filter = new FilterCharacterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public AnubarAmbusher() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(AnubarAmbusher.effect, AnubarAmbusher.filter);
    }
}
