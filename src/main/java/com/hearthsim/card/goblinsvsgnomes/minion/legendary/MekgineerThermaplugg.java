package com.hearthsim.card.goblinsvsgnomes.minion.legendary;

import com.hearthsim.card.classic.minion.common.LeperGnome;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterSummon;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MekgineerThermaplugg extends Minion implements MinionDeadInterface {

    private final static FilterCharacter filter = FilterCharacter.ENEMY_MINIONS;

    public MekgineerThermaplugg() {
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

        if (!MekgineerThermaplugg.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        if (!this.isAlive() || boardState.data_.modelForSide(thisMinionPlayerSide).isBoardFull()) {
            return boardState;
        }

        if (boardState.data_.modelForSide(thisMinionPlayerSide).getNumMinions() >= 7) {
            return boardState;
        }

        EffectCharacter<Minion> effect = new EffectCharacterSummon<>(new LeperGnome());

        return effect.applyEffect(thisMinionPlayerSide, this, boardState);
    }
}
