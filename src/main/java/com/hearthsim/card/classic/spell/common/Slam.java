package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class Slam extends SpellDamageTargetableCard {

    public Slam() {
        super();
    }

    @Deprecated
    public Slam(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Deals 2 damage to a minion.  If the target minion survives, draw a card.
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);
        if (toRet != null && targetMinion.getTotalHealth() > 0) {
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode) toRet).addNumCardsToDraw(1);
            } else {
                toRet = new CardDrawNode(toRet, 1);
            }
        }
        return toRet;
    }
}
