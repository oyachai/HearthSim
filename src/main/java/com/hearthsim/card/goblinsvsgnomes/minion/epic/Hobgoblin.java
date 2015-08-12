package com.hearthsim.card.goblinsvsgnomes.minion.epic;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hobgoblin extends Minion implements MinionSummonedInterface {
    private static final EffectCharacter<Card> effect = new EffectCharacterBuffDelta<>(2, 2);

    private final static FilterCharacter triggerFilter = new FilterCharacter() {
        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected int maxAttack() {
            return 1;
        }

        @Override
        protected int minAttack() {
            return 1;
        }
    };

    public Hobgoblin() {
        super();
    }

    @Override
    public HearthTreeNode minionSummonEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
        if (!Hobgoblin.triggerFilter.targetMatches(thisMinionPlayerSide, this, summonedMinionPlayerSide, summonedMinion, boardState.data_)) {
            return boardState;
        }

        return Hobgoblin.effect.applyEffect(summonedMinionPlayerSide, summonedMinion, boardState);
    }
}
