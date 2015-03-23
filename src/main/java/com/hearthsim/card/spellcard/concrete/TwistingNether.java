package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilterSpellTargetable;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TwistingNether extends SpellCard {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public TwistingNether() {
        super();

        this.minionFilter = MinionFilterSpellTargetable.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card destroys an enemy minion.
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
        HearthTreeNode boardState,
        boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
        for (Minion minion : toRet.data_.getAllMinions()) {
            minion.setHealth((byte)-99);
        }
        return toRet;
    }
}
