package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilterSpellTargetable;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Demonfire extends SpellCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Demonfire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Demonfire() {
        super();

        this.minionFilter = MinionFilterSpellTargetable.ALL_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Deals 2 damage to a minion.  If it's a friendly Demon, give it +2/+2 instead.
     *
     * @param side
     * @param targetMinion The target minion
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
        if (toRet != null) {
            if (isCurrentPlayer(side) && targetMinion.getTribe() == MinionTribe.DEMON) {
                targetMinion.setAttack((byte)(targetMinion.getAttack() + 2));
                targetMinion.setHealth((byte)(targetMinion.getHealth() + 2));
            } else {
                toRet = targetMinion.takeDamageAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, side, boardState, true, false);
            }
        }
        return toRet;
    }
}
