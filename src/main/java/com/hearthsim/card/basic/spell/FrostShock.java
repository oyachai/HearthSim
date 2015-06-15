package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrostShock extends SpellDamageTargetableCard {

    public FrostShock() {
        super();
    }

    @Deprecated
    public FrostShock(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_ENEMIES;
    }

    /**
     *
     * Use the card on the given target
     *
     * Deals 1 damage and freezes an enemy
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
        if (toRet != null) {
            targetMinion.setFrozen(true);
        }

        return toRet;
    }
}
