package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.Collection;

public class ShipsCannon extends Minion implements MinionSummonedInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterDamage(2);

    private final static CharacterFilter effectFilter = CharacterFilter.ALL_ENEMIES;

    private final static CharacterFilter triggerFilter = new CharacterFilter() {

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

        Collection<HearthTreeNode> rngChildren = this.effectRandomCharacterUsingFilter(ShipsCannon.effect, null, ShipsCannon.effectFilter, boardState);
        return this.createRngNodeWithChildren(boardState, rngChildren);
    }
}
