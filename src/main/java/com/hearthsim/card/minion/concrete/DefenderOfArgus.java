package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DefenderOfArgus extends Minion implements MinionUntargetableBattlecry {

    public DefenderOfArgus() {
        super();
    }

    /**
     * Battlecry: Give adjacent minions +1/+1 and Taunt
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        int thisMinionIndex = currentPlayer.getMinions().indexOf(this);
        int numMinions = currentPlayer.getNumMinions();
        if (numMinions > 1) {
            int minionToTheLeft = thisMinionIndex > 0 ? thisMinionIndex - 1 : -1;
            int minionToTheRight = thisMinionIndex < numMinions - 1 ? thisMinionIndex + 1 : -1;
            if (minionToTheLeft >= 0) {
                Minion minionToBuff = toRet.data_.getMinion(PlayerSide.CURRENT_PLAYER, minionToTheLeft);
                minionToBuff.setAttack((byte)(minionToBuff.getAttack() + 1));
                minionToBuff.setHealth((byte)(minionToBuff.getHealth() + 1));
                minionToBuff.setTaunt(true);
            }
            if (minionToTheRight >= 0) {
                Minion minionToBuff = toRet.data_.getMinion(PlayerSide.CURRENT_PLAYER, minionToTheRight);
                minionToBuff.setAttack((byte)(minionToBuff.getAttack() + 1));
                minionToBuff.setHealth((byte)(minionToBuff.getHealth() + 1));
                minionToBuff.setTaunt(true);
            }
        }
        return toRet;
    }

}
