package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hogger extends Minion {

    public Hogger() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = boardModel;
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER && !currentPlayer.isBoardFull()) {
            Minion minion = new Gnoll();
            minion.summonMinionAtEnd(PlayerSide.CURRENT_PLAYER, toRet, false, false);
        }
        return super.endTurn(thisMinionPlayerIndex, toRet);
    }
}
