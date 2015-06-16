package com.hearthsim.card.goblinsvsgnomes.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BolvarFordragon extends Minion implements MinionDeadInterface {

    private final static FilterCharacter filter = FilterCharacter.FRIENDLY_MINIONS;

    public BolvarFordragon() {
        super();
    }

    @Override
    public HearthTreeNode minionDeadEvent(PlayerSide thisMinionPlayerSide, PlayerSide deadMinionPlayerSide, Minion deadMinion, HearthTreeNode boardState) {
        if (!this.setInHand()) {
            return boardState;
        }

        if (!BolvarFordragon.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        // TODO would like to reuse action pattern here but right now that is locked to targeting things on board
        this.addAttack((byte) 1);
        return boardState;
    }
}
