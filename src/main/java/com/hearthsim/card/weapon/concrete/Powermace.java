package com.hearthsim.card.weapon.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.CharacterFilterUntargetedDeathrattle;
import com.hearthsim.event.deathrattle.DeathrattleEffectRandomMinion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;

public class Powermace extends WeaponCard {
    private final static CardEffectCharacter powermaceEffect = new CardEffectCharacterBuffDelta(2, 2);

    private final static CharacterFilterUntargetedDeathrattle filter = new CharacterFilterUntargetedDeathrattle() {
        @Override
        protected boolean includeOwnMinions() { return true; }

        @Override
        protected Minion.MinionTribe tribeFilter() { return Minion.MinionTribe.MECH; }
    };

    public Powermace() {
        super();
        this.deathrattleAction_ = new DeathrattleEffectRandomMinion(Powermace.powermaceEffect, Powermace.filter);
    }
}
