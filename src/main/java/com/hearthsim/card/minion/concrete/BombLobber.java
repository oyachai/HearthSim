package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilter;
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
    public CardEffectCharacter getRandomTargetSecondaryEffect() {
        return null;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
