package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterSummon;

public class RazorfenHunter extends Minion implements MinionBattlecryInterface {

    public RazorfenHunter() {
        super();
    }

    /**
     * Battlecry: Summon a 1/1 Boar
     */
    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return new CardEffectCharacterSummon(new Boar());
    }
}
