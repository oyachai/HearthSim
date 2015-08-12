package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CultMaster extends Minion implements MinionDeadInterface {

    private static final EffectCharacter<Minion> effect = new EffectHeroDraw<>(1);

    private final static FilterCharacter filter = FilterCharacter.FRIENDLY_MINIONS;

    public CultMaster() {
        super();
    }

    /**
     * Draw a card whenever this minion takes damage
     * */
    @Override
    public HearthTreeNode minionDeadEvent(PlayerSide thisMinionPlayerSide, PlayerSide deadMinionPlayerSide, Minion deadMinion, HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }

        if (!CultMaster.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        if (!this.isAlive()) {
            return boardState;
        }

        return CultMaster.effect.applyEffect(thisMinionPlayerSide, CharacterIndex.HERO, boardState);
    }
}
