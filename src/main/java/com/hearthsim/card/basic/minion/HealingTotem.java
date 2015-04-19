package com.hearthsim.card.basic.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HealingTotem extends Minion implements EffectOnResolveAoe<Card> {

    private static final EffectCharacter<Card> effect = new EffectCharacterHeal<>(1);

    public HealingTotem() {
        super();
    }

    /**
     * Called at the end of a turn
     *
     * At the end of your turn, restore 1 Health to all friendly minions
     *
     */
    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel);
        return this.effectAllUsingFilter(this.getAoeEffect(), this.getAoeFilter(), tmpState);
    }

    @Override
    public EffectCharacter<Card> getAoeEffect() {
        return HealingTotem.effect;
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_FRIENDLIES;
    }
}
