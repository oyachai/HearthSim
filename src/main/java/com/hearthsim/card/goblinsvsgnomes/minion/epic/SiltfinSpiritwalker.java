package com.hearthsim.card.goblinsvsgnomes.minion.epic;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHeroDraw;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SiltfinSpiritwalker extends Minion implements MinionDeadInterface {

    private static final EffectCharacter<Minion> effect = new EffectHeroDraw<>(1);

    private final static FilterCharacter filter = new FilterCharacter() {

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.MURLOC;
        }
    };

    public SiltfinSpiritwalker() {
        super();
    }

    @Override
    public HearthTreeNode minionDeadEvent(PlayerSide thisMinionPlayerSide, PlayerSide deadMinionPlayerSide, Minion deadMinion, HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }

        if (!SiltfinSpiritwalker.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        if (!this.isAlive()) {
            return boardState;
        }

        return SiltfinSpiritwalker.effect.applyEffect(thisMinionPlayerSide, CharacterIndex.HERO, boardState);
    }
}
