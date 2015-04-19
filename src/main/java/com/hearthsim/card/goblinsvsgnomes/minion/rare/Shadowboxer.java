package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionHealedInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamage;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.Collection;

public class Shadowboxer extends Minion implements MinionHealedInterface {

    private static final EffectCharacter<Card> effect = new EffectCharacterDamage<>(1);

    private final static FilterCharacter effectFilter = FilterCharacter.ALL_ENEMIES;

    private final static FilterCharacter triggerFilter = FilterCharacter.ALL;

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
