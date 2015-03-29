package com.hearthsim.card.spellcard;

import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.effect.CardEffectCharacter;

public interface SpellAoeInterface {
    public CardEffectCharacter getAoeEffect();
    public MinionFilter getAoeFilter();
}
