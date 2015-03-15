package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.Hound;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class UnleashTheHounds extends SpellCard {

    @Deprecated
    public UnleashTheHounds(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    public UnleashTheHounds() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetEnemyMinions = false;
        this.canTargetOwnMinions = false;
    }

    /**
     *
     * Use the card on the given target
     *
     * @param side
     * @param targetMinion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param singleRealizationOnly
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
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            int numHoundsToSummon = waitingPlayer.getNumMinions();
            if (numHoundsToSummon + currentPlayer.getNumMinions() > 7)
                numHoundsToSummon = 7 - currentPlayer.getNumMinions();
            for (int indx = 0; indx < numHoundsToSummon; ++indx) {
                Minion placementTarget = currentPlayer.getNumMinions() > 0 ? currentPlayer.getMinions().getLast() : currentPlayer.getHero();
                toRet = new Hound().summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, false, singleRealizationOnly);
            }
        }
        return toRet;
    }
}
