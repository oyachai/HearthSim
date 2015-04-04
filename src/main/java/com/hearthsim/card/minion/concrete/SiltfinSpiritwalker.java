package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SiltfinSpiritwalker extends Minion implements MinionDeadInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterDraw(1);

    private final static CharacterFilter filter = new CharacterFilter() {

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

    /**
     * Draw a card whenever this minion takes damage
     * */
    @Override
    public HearthTreeNode minionDeadEvent(PlayerSide thisMinionPlayerSide, PlayerSide deadMinionPlayerSide, Minion deadMinion, HearthTreeNode boardState) {
        if (this.isInHand()) {
            return boardState;
        }

        if (!SiltfinSpiritwalker.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        if (!this.isAlive()) {
            return boardState;
        }

        return SiltfinSpiritwalker.effect.applyEffect(thisMinionPlayerSide, this, thisMinionPlayerSide, 0, boardState);
    }
}
