package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectCharacterSummonNew<T extends Card> implements EffectCharacter<T> {
    private Class<? extends Minion> minionClass;

    private int count;

    private boolean atEnd;

    public EffectCharacterSummonNew(Class<? extends Minion> minionClass) {
        this(minionClass, 1);
    }

    public EffectCharacterSummonNew(Class<? extends Minion> minionClass, int count) {
        this(minionClass, count, false);
    }

    public EffectCharacterSummonNew(Class<? extends Minion> minionClass, int count, boolean atEnd) {
        this.minionClass = minionClass;
        this.count = count;
        this.atEnd = atEnd;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
        // TODO this kind of check belongs in a pre-check/filter
        PlayerModel player = boardState.data_.modelForSide(targetSide);
        if (player.isBoardFull()) {
            return null;
        }

        for (int i = 0; i < this.count; i++) {
            try {
                Minion summon = this.minionClass.newInstance();
                summon.hasBeenUsed(true);
                if (atEnd) {
                    boardState = summon.summonMinionAtEnd(targetSide, boardState, true);
                } else {
                    boardState = summon.summonMinion(targetSide, targetCharacterIndex, boardState, true);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            player = boardState.data_.modelForSide(targetSide);
            if (player.isBoardFull()) {
                break;
            }
        }

        // if no origin is set then we have no idea whether we are in the original state. copy our base minion and summon a copy.
        // this is used for Minions with RNG battlecries (e.g. Bomb Lobber)
//        if (origin == null) {
//            summon = (Minion)minion.deepCopy();
//        }
        return boardState;
    }
}
