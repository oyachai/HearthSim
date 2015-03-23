package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilterSpellTargetable;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class LayOnHands extends SpellCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public LayOnHands(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public LayOnHands() {
        super();

        //Let's assume that it is never beneficial to heal an opponent... though this may not strictly be true under some very corner cases (e.g., with a Northshire Cleric)
        this.minionFilter = MinionFilterSpellTargetable.FRIENDLY_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Restore 8 Health and draw 3 cards
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
        toRet = targetMinion.takeHealAndNotify((byte) 8, side, boardState);
        if (toRet instanceof CardDrawNode)
            ((CardDrawNode) toRet).addNumCardsToDraw(3);
        else
            toRet = new CardDrawNode(toRet, 3); //draw three cards

        return toRet;
    }
}
