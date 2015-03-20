package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 3/20/15.
 */
public class ColdBlood extends SpellCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public ColdBlood(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public ColdBlood() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetOwnHero = false;
    }

    /**
     *
     * Use the card on the given target
     *
     * Give a minion +2 Attack. Combo: +4 Attack instead.
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
        byte buffAmount = boardState.data_.getCurrentPlayer().isComboEnabled() ? (byte)4 : (byte)2;

        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
        if (toRet != null)
            targetMinion.addAttack(buffAmount);
        return toRet;
    }
}
