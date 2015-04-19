package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;

public class BombLobber extends Minion implements EffectOnResolveRandomCharacter {
    private static final EffectCharacter effect = new EffectCharacterDamage(4);

    @Override
    public EffectCharacter getRandomTargetEffect() {
        return BombLobber.effect;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
