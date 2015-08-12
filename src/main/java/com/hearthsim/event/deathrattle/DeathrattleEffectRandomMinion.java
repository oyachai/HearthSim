package com.hearthsim.event.deathrattle;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacterUntargetedDeathrattle;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
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
    public HearthTreeNode performAction(CharacterIndex originIndex, PlayerSide playerSide, HearthTreeNode boardState) {
        // TODO could probably be faster and belongs in a more common location
        List<CharacterIndex.CharacterLocation> locations = new ArrayList<>();
        for (CharacterIndex.CharacterLocation location : boardState.data_) {
            if (this.filter.targetMatches(playerSide, null, location.getPlayerSide(), location.getIndex(), boardState.data_)) {
                locations.add(location);
            }
        }

        switch (locations.size()) {
            case 0: // no targets, do nothing
                break;
            case 1: // one target, no RNG needed
                this.effect.applyEffect(locations.get(0).getPlayerSide(), locations.get(0).getIndex(), boardState);
                break;
            default: // more than 1 option, generate all possible futures
                RandomEffectNode rngNode = new RandomEffectNode(boardState, boardState.getAction());
                for (CharacterIndex.CharacterLocation location : locations) {
                    HearthTreeNode newState = new HearthTreeNode(rngNode.data_.deepCopy());
                    this.effect.applyEffect(location.getPlayerSide(), location.getIndex(), newState);

                    BoardStateFactoryBase.handleDeadMinions(newState);
                    rngNode.addChild(newState);
                }

                boardState = rngNode;
                break;
        }
        return boardState;
    }

}
