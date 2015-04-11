package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;

public class StormpikeCommando extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Deal 2 damage to a chosen target
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyHero() { return true; }
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnHero() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterDamage(2);

    public StormpikeCommando() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return StormpikeCommando.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return StormpikeCommando.battlecryAction;
    }
}
