package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientOfLore extends Minion {

    private static final byte HEAL_AMOUNT = 5;

    public AncientOfLore() {
        super();
    }

    /**
     *
     * Choose one: Draw 2 cards; or Restore 5 health
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public HearthTreeNode use_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState, boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);

        if (toRet != null) {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);

            int thisMinionIndex = currentPlayer.getMinions().indexOf(this);
            toRet.addChild(new CardDrawNode(new HearthTreeNode(toRet.data_.deepCopy()), 2));

            HearthTreeNode newState = new HearthTreeNode(toRet.data_.deepCopy());
            newState = currentPlayer.getHero().takeHealAndNotify(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, newState);
            toRet.addChild(newState);

            for (int index = 0; index < currentPlayer.getNumMinions(); ++index) {
                if (index != thisMinionIndex) {
                    newState = new HearthTreeNode(toRet.data_.deepCopy());
                    newState = currentPlayer.getMinions().get(index).takeHealAndNotify(HEAL_AMOUNT, PlayerSide.CURRENT_PLAYER, newState);
                    toRet.addChild(newState);
                }
            }

            newState = new HearthTreeNode(toRet.data_.deepCopy());
            newState = waitingPlayer.getHero().takeHealAndNotify(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, newState);
            toRet.addChild(newState);

            for (int index = 0; index < waitingPlayer.getNumMinions(); ++index) {
                newState = new HearthTreeNode(toRet.data_.deepCopy());
                newState = waitingPlayer.getMinions().get(index).takeHealAndNotify(HEAL_AMOUNT, PlayerSide.WAITING_PLAYER, newState);
                toRet.addChild(newState);
            }

        }
        return toRet;
    }
}
