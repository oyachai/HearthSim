package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.MinionFilterUntargetedDeathrattle;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;

public class AnubarAmbusher extends Minion {

    private final static CardEffectCharacter effect = CardEffectCharacter.BOUNCE;

    private final static MinionFilterUntargetedDeathrattle filter = new MinionFilterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() { return true; }
    };

    public AnubarAmbusher() {
        super();
        deathrattleAction_ = new DeathrattleEffectRandomMinion(AnubarAmbusher.effect, AnubarAmbusher.filter);
    }
}
