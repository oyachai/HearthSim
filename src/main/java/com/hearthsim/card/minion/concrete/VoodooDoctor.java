package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterHeal;

public class VoodooDoctor extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Restore 2 health
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyHero() { return true; }
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnHero() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterHeal(2);

    public VoodooDoctor() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return VoodooDoctor.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return VoodooDoctor.battlecryAction;
    }
}
