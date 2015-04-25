package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveRandomCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class StampedingKodo extends Minion implements EffectOnResolveRandomCharacter {

    public StampedingKodo() {
        super();
    }

    @Override
    public EffectCharacter getRandomTargetEffect() {
        return EffectCharacter.DESTROY;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return new FilterCharacter() {
            @Override
            protected boolean includeEnemyMinions() {
                return true;
            }

            @Override
            protected int maxAttack() {
                return 2;
            }
        };
    }
}
