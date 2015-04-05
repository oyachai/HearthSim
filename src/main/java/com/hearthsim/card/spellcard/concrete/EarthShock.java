package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EarthShock extends SpellDamageTargetableCard {

    public EarthShock() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    @Deprecated
    public EarthShock(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     *
     * Use the card on the given target
     *
     * Silence a minion, then deal 1 damage
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
            HearthTreeNode boardState, boolean singleRealizationOnly)
        throws HSException {
        targetMinion.silenced(side, boardState.data_);
        return super.use_core(side, targetMinion, boardState, singleRealizationOnly);
    }
}
