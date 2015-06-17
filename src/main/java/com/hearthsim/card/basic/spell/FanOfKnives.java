package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class FanOfKnives extends SpellDamage implements EffectOnResolveAoe {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public FanOfKnives(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     * Defaults to hasBeenUsed = false
     */
    public FanOfKnives() {
        super();
    }

    /**
     * Use the card on the given target
     * This card damages all enemy minions by 1 and draws a card
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);

        if (toRet != null) {
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode)toRet).addNumCardsToDraw(1);
            } else {
                toRet = new CardDrawNode(toRet, 1); // draw a card
            }
        }
        return toRet;
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ENEMY_MINIONS;
    }
}
