package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacterUntargetedDeathrattle;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.List;

public class DeathrattleEffectRandomMinion extends DeathrattleAction {
    private final EffectCharacter effect;
    private final FilterCharacterUntargetedDeathrattle filter;

    public DeathrattleEffectRandomMinion(EffectCharacter effect, FilterCharacterUntargetedDeathrattle filter) {
        this.effect = effect;
        this.filter = filter;
    }

    @Override
    public HearthTreeNode performAction(Card origin, PlayerSide playerSide, HearthTreeNode boardState, boolean singleRealizationOnly) {
        // TODO could probably be faster and belongs in a more common location
        List<BoardModel.CharacterLocation> locations = new ArrayList<>();
        for (BoardModel.CharacterLocation location : boardState.data_) {
            if (this.filter.targetMatches(playerSide, origin, location.getPlayerSide(), location.getIndex(), boardState.data_)) {
                locations.add(location);
            }
        }

        switch (locations.size()) {
            case 0: // no targets, do nothing
                break;
            case 1: // one target, no RNG needed
                this.effect.applyEffect(playerSide, origin, locations.get(0).getPlayerSide(), locations.get(0).getIndex(), boardState);
                break;
            default: // more than 1 option, generate all possible futures
                RandomEffectNode rngNode = new RandomEffectNode(boardState, boardState.getAction());
                for (BoardModel.CharacterLocation location : locations) {
                    HearthTreeNode newState = new HearthTreeNode(rngNode.data_.deepCopy());
                    this.cleanupBoard(playerSide, origin, boardState.data_, newState.data_);

                    this.effect.applyEffect(playerSide, origin, location.getPlayerSide(), location.getIndex(), newState);

                    rngNode.addChild(newState);
                }

                boardState = rngNode;
                break;
        }
        return boardState;
    }

    // TODO need to do this manually for now. we should handle this in the death handler
    private void cleanupBoard(PlayerSide originSide, Card origin, BoardModel parent, BoardModel child) {
        if (origin instanceof Minion) {
            int originIndex = parent.modelForSide(originSide).getIndexForCharacter((Minion)origin);
            child.removeMinion(originSide, originIndex - 1);
        }
    }
}
