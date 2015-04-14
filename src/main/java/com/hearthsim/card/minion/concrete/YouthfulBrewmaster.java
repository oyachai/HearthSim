package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

public class YouthfulBrewmaster extends Minion implements MinionBattlecryInterface {

    private final static FilterCharacterTargetedBattlecry filter = new FilterCharacterTargetedBattlecry() {
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    private final static CardEffectCharacter effect = CardEffectCharacter.BOUNCE;

    public YouthfulBrewmaster() {
        super();
    }

    @Override
    public FilterCharacter getBattlecryFilter() {
        return YouthfulBrewmaster.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return YouthfulBrewmaster.effect;
    }
}
