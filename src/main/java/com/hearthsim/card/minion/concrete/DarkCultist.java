package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilterUntargetedDeathrattle;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;

public class DarkCultist extends Minion {

    private final static CardEffectCharacter darkCultistEffect = new CardEffectCharacterBuffDelta(0, 3);

    private final static CharacterFilterUntargetedDeathrattle filter = new CharacterFilterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public DarkCultist() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(DarkCultist.darkCultistEffect, DarkCultist.filter);
    }
}
