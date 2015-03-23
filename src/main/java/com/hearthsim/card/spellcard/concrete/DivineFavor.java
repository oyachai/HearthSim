package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilterSpellTargetable;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class DivineFavor extends SpellCard {
    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public DivineFavor(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public DivineFavor() {
        super();

        this.minionFilter = MinionFilterSpellTargetable.SELF;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        int numCardsToDraw = boardModel.modelForSide(PlayerSide.WAITING_PLAYER).getHand().size() - boardModel.modelForSide(PlayerSide.CURRENT_PLAYER).getHand().size() + 1;
        if (numCardsToDraw < 1) {
            return false;
        }

        return true;
    }

    /**
     *
     * Use the card on the given target
     *
     * Draw cards until you have as many in hand as your opponent
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
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        int numCardsToDraw = waitingPlayer.getHand().size() - currentPlayer.getHand().size() + 1;
        if (numCardsToDraw < 1) {
            return null;
        }

        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
        if (toRet != null) {
            if (toRet instanceof CardDrawNode)
                ((CardDrawNode) toRet).addNumCardsToDraw(numCardsToDraw);
            else
                toRet = new CardDrawNode(toRet, numCardsToDraw); //draw two cards
        }
        return toRet;
    }
}
