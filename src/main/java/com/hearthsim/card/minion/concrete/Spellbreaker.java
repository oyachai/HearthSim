package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

public class Spellbreaker extends Minion implements MinionBattlecryInterface {

    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() {
            return true;
        }
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter battlecryAction = CardEffectCharacter.SILENCE;

    public Spellbreaker() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return Spellbreaker.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return Spellbreaker.battlecryAction;
    }
}
