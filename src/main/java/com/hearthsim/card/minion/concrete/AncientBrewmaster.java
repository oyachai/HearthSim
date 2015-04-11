package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;

public class AncientBrewmaster extends Minion implements MinionBattlecryInterface {

    private final static CharacterFilterTargetedBattlecry filter = new CharacterFilterTargetedBattlecry() {
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter effect = CardEffectCharacter.BOUNCE;

    public AncientBrewmaster() {
        super();
    }

    @Override
    public CharacterFilter getBattlecryFilter() {
        return AncientBrewmaster.filter;
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return AncientBrewmaster.effect;
    }
}
