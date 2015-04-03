package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilterUntargetedDeathrattle;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.List;

public class DeathrattleEffectRandomMinion extends DeathrattleAction {
    private final CardEffectCharacter effect;
    private final CharacterFilterUntargetedDeathrattle filter;

    public DeathrattleEffectRandomMinion(CardEffectCharacter effect, CharacterFilterUntargetedDeathrattle filter) {
        this.effect = effect;
        this.filter = filter;
    }

    @Override
    public HearthTreeNode performAction(Card origin, PlayerSide playerSide, HearthTreeNode boardState, boolean singleRealizationOnly) {
        PlayerModel owner = boardState.data_.modelForSide(playerSide);
        PlayerModel opposing = boardState.data_.modelForSide(playerSide.getOtherPlayer());

        // TODO could probably be faster and belongs in a more common location
        List<Minion> friendlyTargets = new ArrayList<>();
        for (Minion minion : owner.getMinions()) {
            if (this.filter.targetMatches(playerSide, origin, playerSide, minion, boardState.data_)) {
                friendlyTargets.add(minion);
            }
        }

        List<Minion> enemyTargets = new ArrayList<>();
        for (Minion minion : opposing.getMinions()) {
            if (this.filter.targetMatches(playerSide, origin, playerSide.getOtherPlayer(), minion, boardState.data_)) {
                enemyTargets.add(minion);
            }
        }

        int totalTargets = friendlyTargets.size() + enemyTargets.size();
        Minion targetMinion;
        switch (totalTargets) {
            case 0: // no targets, do nothing
                break;
            case 1: // one target, no RNG needed
                targetMinion = friendlyTargets.size() > 0 ? friendlyTargets.get(0) : enemyTargets.get(0);
                this.effect.applyEffect(playerSide, origin, playerSide, targetMinion, boardState);
                break;
            default: // more than 1 option, generate all possible futures
                RandomEffectNode rngNode = new RandomEffectNode(boardState, boardState.getAction());
                for (Minion possibleTarget : friendlyTargets) {
                    int targetIndex = owner.getIndexForCharacter(possibleTarget);

                    HearthTreeNode newState = new HearthTreeNode(rngNode.data_.deepCopy());
                    this.cleanupBoard(playerSide, origin, boardState.data_, newState.data_);

                    this.effect.applyEffect(playerSide, origin, playerSide, targetIndex, newState);

                    rngNode.addChild(newState);
                }

                for (Minion possibleTarget : enemyTargets) {
                    int targetIndex = opposing.getIndexForCharacter(possibleTarget);

                    HearthTreeNode newState = new HearthTreeNode(rngNode.data_.deepCopy());
                    this.cleanupBoard(playerSide, origin, boardState.data_, newState.data_);

                    this.effect.applyEffect(playerSide, origin, playerSide.getOtherPlayer(), targetIndex, newState);

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
