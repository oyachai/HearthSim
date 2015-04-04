package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDeadInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterSummon;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MekgineerThermaplugg extends Minion implements MinionDeadInterface {

    private final static CharacterFilter filter = CharacterFilter.ENEMY_MINIONS;

    public MekgineerThermaplugg() {
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

        if (!MekgineerThermaplugg.filter.targetMatches(thisMinionPlayerSide, this, deadMinionPlayerSide, deadMinion, boardState.data_)) {
            return boardState;
        }

        if (!this.isAlive()) {
            return boardState;
        }

        CardEffectCharacter effect = new CardEffectCharacterSummon(new LeperGnome());
        return effect.applyEffect(thisMinionPlayerSide, this, thisMinionPlayerSide, this, boardState);
    }
}
