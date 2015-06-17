package com.hearthsim.event.filter;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

@FunctionalInterface
public interface FilterCharacterInterface extends FilterInterface<Card, CharacterIndex> {
    public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board);

    public default boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, CharacterIndex targetCharacterIndex, BoardModel board) {
        Minion targetCharacter = board.getCharacter(targetSide, targetCharacterIndex);
        return this.targetMatches(originSide, origin, targetSide, targetCharacter, board);
    }

    public default int countMatchesForBoard(PlayerSide originSide, Card origin, BoardModel board) {
        int matches = 0;
        for (CharacterIndex.CharacterLocation location : board) {
            if (this.targetMatches(originSide, origin, location.getPlayerSide(), location.getIndex(), board)) {
                matches++;
            }
        }
        return matches;
    }
}
