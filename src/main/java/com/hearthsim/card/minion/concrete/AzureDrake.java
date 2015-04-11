package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;

public class AzureDrake extends Minion implements MinionBattlecryInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterDraw(1);

    public AzureDrake() {
        super();
    }

    /**
     * Battlecry: Draw one card
     */
    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return AzureDrake.effect;
    }
}
