package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.CardDrawNode;

public class BattleRage extends SpellTargetableCard {

    private static final EffectCharacter effect = (targetSide, targetCharacterIndex, boardState) -> {
        PlayerModel playerModel = boardState.data_.modelForSide(targetSide);
        Hero hero = playerModel.getHero();
        Iterable<Minion> minions = playerModel.getMinions();
        int numCardsToDraw = hero.getTotalHealth() < hero.getTotalMaxHealth() ? 1 : 0;
        for (Minion minion : minions) {
            numCardsToDraw += minion.getTotalHealth() < minion.getTotalMaxHealth() ? 1 : 0;
        }
        if (boardState instanceof CardDrawNode) {
            ((CardDrawNode) boardState).addNumCardsToDraw(numCardsToDraw);
        } else {
            boardState = new CardDrawNode(boardState, numCardsToDraw); //draw two cards
        }
        return boardState;
    };

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public BattleRage(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public BattleRage() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Draw a card for each damaged friendly character
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        return BattleRage.effect;
    }
}
