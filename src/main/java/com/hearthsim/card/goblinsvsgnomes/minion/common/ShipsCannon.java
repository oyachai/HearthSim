package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.Collection;

public class ShipsCannon extends Minion implements MinionSummonedInterface {

    private static final EffectCharacter<Card> effect = new EffectCharacterDamage<>(2);

    private final static FilterCharacter effectFilter = FilterCharacter.ALL_ENEMIES;

    private final static FilterCharacter triggerFilter = new FilterCharacter() {

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }

        @Override
        protected MinionTribe tribeFilter() {
            return MinionTribe.PIRATE;
        }
    };

    public ShipsCannon() {
        super();
    }

    @Override
    public HearthTreeNode minionSummonEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
        if (!ShipsCannon.triggerFilter.targetMatches(thisMinionPlayerSide, this, summonedMinionPlayerSide, summonedMinion, boardState.data_)) {
            return boardState;
        }

        Collection<HearthTreeNode> rngChildren = this.effectRandomCharacterUsingFilter(ShipsCannon.effect, null, ShipsCannon.effectFilter, summonedMinionPlayerSide, boardState);
        return this.createRngNodeWithChildren(boardState, rngChildren);
    }
}
