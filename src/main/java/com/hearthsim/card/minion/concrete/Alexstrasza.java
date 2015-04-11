package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuff;

public class Alexstrasza extends Minion implements MinionBattlecryInterface {

    /**
     * Battlecry: Set a hero's remaining health to 15
     */
    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyHero() { return true; }
        protected boolean includeOwnHero() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacterBuff(0, 15);

    public Alexstrasza() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return Alexstrasza.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return Alexstrasza.battlecryAction;
    }
}
