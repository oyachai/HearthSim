package com.hearthsim.card.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacterInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

@Deprecated
public interface MinionTargetableBattlecry extends MinionBattlecryInterface {
    /**
     * Derived classes should implement this function for targtable battlecries.
     *
     * @param targetCharacterIndex
     * @param boardState
     * @return
     */
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState);

    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, CharacterIndex targetCharacterIndex, BoardModel board);

    @Override
    public default FilterCharacterInterface getBattlecryFilter() {
        return (PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) -> this.canTargetWithBattlecry(originSide, origin, targetSide, board.modelForSide(targetSide).getIndexForCharacter(targetCharacter), board);
    }

    @Override
    public default EffectCharacter<Minion> getBattlecryEffect() {
        return this::useTargetableBattlecry_core;
    }
}
