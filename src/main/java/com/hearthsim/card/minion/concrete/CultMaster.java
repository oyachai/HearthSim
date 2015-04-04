package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CultMaster extends Minion implements MinionDeadInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterDraw(1);

    private final static CharacterFilter filter = CharacterFilter.FRIENDLY_MINIONS;

    public CultMaster() {
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

        if (!CultMaster.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        if (!this.isAlive()) {
            return boardState;
        }

        return CultMaster.effect.applyEffect(thisMinionPlayerSide, this, thisMinionPlayerSide, 0, boardState);
    }
}
