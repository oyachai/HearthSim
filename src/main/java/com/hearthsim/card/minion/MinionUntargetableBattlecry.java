package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public interface MinionUntargetableBattlecry extends MinionTargetableBattlecry {
    public HearthTreeNode useUntargetableBattlecry_core(int minionPlacementIndex, HearthTreeNode boardState, boolean singleRealizationOnly);

    @Override
    public default HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        return this.getTargetableBattlecryEffect().applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
    }

    @Override
    public default boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        Minion targetMinion = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return this.getTargetableBattlecryFilter().targetMatches(originSide, origin, targetSide, targetMinion, board);
    }

    @Override
    public default CharacterFilter getTargetableBattlecryFilter() {
        return CharacterFilter.SELF;
    }

    @Override
    public default CardEffectCharacter<Minion> getTargetableBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) -> this.useUntargetableBattlecry_core(targetCharacterIndex, boardState, false);
    }
}
