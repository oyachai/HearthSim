package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;

public class BombLobber extends Minion implements CardEffectOnResolveRandomCharacterInterface {
    private static final CardEffectCharacter effect = new CardEffectCharacterDamage(4);

    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return BombLobber.effect;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
