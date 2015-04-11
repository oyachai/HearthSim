package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterInterface;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@Deprecated
public interface MinionTargetableBattlecry extends MinionBattlecryInterface {
    /**
     * Derived classes should implement this function for targtable battlecries.
     *
     * @param originSide
     * @param origin
     * @param targetCharacterIndex
     * @param boardState
     * @return
     */
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState);

    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board);

    @Override
    public default CharacterFilterInterface getBattlecryFilter() {
        return (PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) -> this.canTargetWithBattlecry(originSide, origin, targetSide, board.modelForSide(targetSide).getIndexForCharacter(targetCharacter), board);
    }

    @Override
    public default CardEffectCharacter<Minion> getBattlecryEffect() {
        return this::useTargetableBattlecry_core;
    }
}
