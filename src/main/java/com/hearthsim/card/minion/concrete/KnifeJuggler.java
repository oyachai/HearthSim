package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.Collection;

public class KnifeJuggler extends Minion implements MinionSummonedInterface {

    private static final CardEffectCharacter<Card> effect = new CardEffectCharacterDamage<>(1);

    private final static FilterCharacter effectFilter = FilterCharacter.ALL_ENEMIES;

    private final static FilterCharacter triggerFilter = FilterCharacter.FRIENDLY_MINIONS;

    public KnifeJuggler() {
        super();
    }

    @Override
    public HearthTreeNode minionSummonEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
        if (!KnifeJuggler.triggerFilter.targetMatches(thisMinionPlayerSide, this, summonedMinionPlayerSide, summonedMinion, boardState.data_)) {
            return boardState;
        }

        Collection<HearthTreeNode> rngChildren = this.effectRandomCharacterUsingFilter(KnifeJuggler.effect, null, KnifeJuggler.effectFilter, boardState);
        return this.createRngNodeWithChildren(boardState, rngChildren);
    }
}
