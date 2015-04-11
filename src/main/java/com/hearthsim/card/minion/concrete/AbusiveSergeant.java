package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilterInterface;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffTemp;

public class AbusiveSergeant extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Give a minion +2 attack this turn
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter<Minion> battlecryAction = new CardEffectCharacterBuffTemp<>(2);

    public AbusiveSergeant() {
        super();
    }

    @Override
    public CharacterFilterInterface getBattlecryFilter() {
        return AbusiveSergeant.filter;
    }

    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return AbusiveSergeant.battlecryAction;
    }
}
