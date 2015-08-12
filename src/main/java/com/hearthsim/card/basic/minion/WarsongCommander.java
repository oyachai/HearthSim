package com.hearthsim.card.basic.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class WarsongCommander extends Minion implements MinionSummonedInterface {
    private static final EffectCharacter<Card> effect = EffectCharacter.GIVE_CHARGE;

    private final static FilterCharacter triggerFilter = new FilterCharacter() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected int maxAttack() {
            return 3;
        }

        @Override
        protected boolean excludeSource() {
            return true;
        }
    };

    public WarsongCommander() {
        super();
    }

    @Override
    public HearthTreeNode minionSummonEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
        if (!WarsongCommander.triggerFilter.targetMatches(thisMinionPlayerSide, this, summonedMinionPlayerSide, summonedMinion, boardState.data_)) {
            return boardState;
        }

        return WarsongCommander.effect.applyEffect(summonedMinionPlayerSide, summonedMinion, boardState);
    }
}
