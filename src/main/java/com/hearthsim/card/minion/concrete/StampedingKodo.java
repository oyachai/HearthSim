package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveRandomCharacterInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.List;

public class StampedingKodo extends Minion implements CardEffectOnResolveRandomCharacterInterface {

    public StampedingKodo() {
        super();
    }

    @Override
    public CardEffectCharacter getRandomTargetEffect() {
        return CardEffectCharacter.DESTROY;
    }

    @Override
    public CharacterFilter getRandomTargetFilter() {
        return new CharacterFilter() {
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
