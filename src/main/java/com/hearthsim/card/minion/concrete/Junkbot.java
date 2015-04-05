package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Junkbot extends Minion implements MinionDeadInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterBuffDelta(2, 2);

    private final static CharacterFilter filter = new CharacterFilter() {

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
        if (this.isInHand()) {
            return boardState;
        }

        if (!Junkbot.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        return Junkbot.effect.applyEffect(thisMinionPlayerSide, this, thisMinionPlayerSide, this, boardState);
    }
}
