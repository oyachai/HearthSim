package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SunfuryProtector extends Minion implements MinionUntargetableBattlecry {

    public SunfuryProtector() {
        super();
    }

    /**
     * Battlecry: Give adjacent minions Taunt
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
                toRet.data_.getMinion(PlayerSide.CURRENT_PLAYER, minionToTheLeft).setTaunt(true);
            }
            if (minionToTheRight >= 0) {
                toRet.data_.getMinion(PlayerSide.CURRENT_PLAYER, minionToTheRight).setTaunt(true);
            }
        }
        return toRet;
    }
}
