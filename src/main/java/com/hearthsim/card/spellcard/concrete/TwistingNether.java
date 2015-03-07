package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
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

        this.canTargetEnemyHero = false;
        this.canTargetEnemyMinions = false;
        this.canTargetOwnMinions = false;
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
        Deck deckPlayer0,
        Deck deckPlayer1,
        boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
        if (toRet != null) {
            for (BoardModel.MinionPlayerPair minionIdPair : toRet.data_.getAllMinionsFIFOList()) {
                minionIdPair.getMinion().setHealth((byte)-99);
            }
        }
        return toRet;
    }
}
