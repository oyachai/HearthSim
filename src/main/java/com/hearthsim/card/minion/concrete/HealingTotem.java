package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterHeal;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HealingTotem extends Minion implements CardEffectAoeInterface {

    private static final CardEffectCharacter effect = new CardEffectCharacterHeal(1);

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
    public CardEffectCharacter getAoeEffect() {
        return HealingTotem.effect;
    }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ALL_FRIENDLIES;
    }
}
