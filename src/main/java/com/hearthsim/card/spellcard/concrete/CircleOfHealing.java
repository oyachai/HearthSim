package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CircleOfHealing extends SpellCard {

    private static final byte HEAL_AMOUNT = 4;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public CircleOfHealing(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public CircleOfHealing() {
        super();

        this.canTargetEnemyHero = false;
        this.canTargetEnemyMinions = false;
        this.canTargetOwnMinions = false;
    }

    /**
     *
     * Circle of Healing
     *
     * Heals all minions for 4
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
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        for (Minion minion : currentPlayer.getMinions()) {
            toRet = minion.takeHeal(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, toRet);
        }

        for (Minion minion : waitingPlayer.getMinions()) {
            toRet = minion.takeHeal(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, toRet);
        }

        return toRet;
    }
}
