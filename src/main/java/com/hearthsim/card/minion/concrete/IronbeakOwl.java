package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

public class IronbeakOwl extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Silence a minion
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = CardEffectCharacter.SILENCE;

    public IronbeakOwl() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return IronbeakOwl.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return IronbeakOwl.battlecryAction;
    }
}
