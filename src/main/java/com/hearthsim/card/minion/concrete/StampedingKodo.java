package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class StampedingKodo extends Minion implements CardEffectOnResolveRandomCharacterInterface {

    public StampedingKodo() {
        super();
    }

    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return CardEffectCharacter.DESTROY;
    }

    @Override
    public FilterCharacter getRandomTargetFilter() {
        return new FilterCharacter() {
            @Override
            protected boolean includeEnemyMinions() {
                return true;
            }

            @Override
            public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
                if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                    return false;
                }

                return targetCharacter.getTotalAttack() <= 2;
            }
        };
    }
}
