package com.hearthsim.event.effect;

import com.hearthsim.card.Card;

public class SpellEffectCharacterDamage<T extends Card> extends CardEffectCharacterDamage<T> {
    public SpellEffectCharacterDamage(int damage) {
        super(damage, true);
    }
}
