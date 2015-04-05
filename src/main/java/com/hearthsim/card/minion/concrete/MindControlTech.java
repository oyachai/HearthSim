package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MindControlTech extends Minion implements CardEffectOnResolveRandomCharacterInterface {

    private static final CardEffectCharacter effect = CardEffectCharacter.MIND_CONTROL;

    private static final CharacterFilter filter = new CharacterFilter() {

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            return board.modelForSide(targetSide).getNumMinions() >= 4;
        }
    };

    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return MindControlTech.effect;
    }

    @Override
    public CardEffectCharacter getRandomTargetSecondaryEffect() {
        return null;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return MindControlTech.filter;
    }
}
