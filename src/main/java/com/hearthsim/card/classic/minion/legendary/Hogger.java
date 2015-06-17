package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.classic.minion.common.Gnoll;
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
        PlayerModel currentPlayer = boardModel.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER && !currentPlayer.isBoardFull()) {
            Minion minion = new Gnoll();
            minion.summonMinionAtEnd(PlayerSide.CURRENT_PLAYER, boardModel, false);
        }
        return super.endTurn(thisMinionPlayerIndex, boardModel);
    }
}
