package com.hearthsim.card.goblinsvsgnomes.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Junkbot extends Minion implements MinionDeadInterface {

    private static final EffectCharacter<Minion> effect = new EffectCharacterBuffDelta<>(2, 2);

    private final static FilterCharacter filter = new FilterCharacter() {

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.MECH;
        }
    };

    public Junkbot() {
        super();
    }

    @Override
    public HearthTreeNode minionDeadEvent(PlayerSide thisMinionPlayerSide, PlayerSide deadMinionPlayerSide, Minion deadMinion, HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }

        if (!Junkbot.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        return Junkbot.effect.applyEffect(thisMinionPlayerSide, this, boardState);
    }
}
