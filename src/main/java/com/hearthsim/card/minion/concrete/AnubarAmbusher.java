package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilterUntargetedDeathrattle;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.event.effect.CardEffectCharacter;

public class AnubarAmbusher extends Minion {

    private final static CardEffectCharacter effect = CardEffectCharacter.BOUNCE;

    private final static CharacterFilterUntargetedDeathrattle filter = new CharacterFilterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public AnubarAmbusher() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(AnubarAmbusher.effect, AnubarAmbusher.filter);
    }
}
