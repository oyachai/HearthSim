package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AncientOfWar extends Minion {

    public AncientOfWar() {
        super();
    }

    /**
     *
     * Choose one: +5 health and Taunt; or +5 Attack
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

            int thisMinionIndex = currentPlayer.getMinions().indexOf(this);
            {
                HearthTreeNode newState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
                Minion newMinion = newState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions().get(thisMinionIndex);
                newMinion.setTaunt(true);
                newMinion.setMaxHealth((byte) 10);
                newMinion.setHealth((byte) 10);
            }
            {
                HearthTreeNode newState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
                Minion newMinion = newState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions().get(thisMinionIndex);
                newMinion.setAttack((byte) 10);
            }
        }
        return toRet;
    }
}
