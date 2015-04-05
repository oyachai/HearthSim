package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionHealedInterface;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDamage;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.Collection;

public class Shadowboxer extends Minion implements MinionHealedInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterDamage(1);

    private final static CharacterFilter effectFilter = CharacterFilter.ALL_ENEMIES;

    private final static CharacterFilter triggerFilter = CharacterFilter.ALL;

    public Shadowboxer() {
        super();
    }

    @Override
    public HearthTreeNode minionHealedEvent(PlayerSide thisMinionPlayerSide, PlayerSide healedMinionPlayerSide, Minion healedMinion, HearthTreeNode boardState) {
        if (!Shadowboxer.triggerFilter.targetMatches(thisMinionPlayerSide, this, healedMinionPlayerSide, healedMinion, boardState.data_)) {
            return boardState;
        }

        Collection<HearthTreeNode> rngChildren = this.effectRandomCharacterUsingFilter(Shadowboxer.effect, null, Shadowboxer.effectFilter, thisMinionPlayerSide, boardState);
        return this.createRngNodeWithChildren(boardState, rngChildren);
    }
}
