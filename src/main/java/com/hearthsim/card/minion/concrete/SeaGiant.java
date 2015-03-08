package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;

public class SeaGiant extends Minion {

    public SeaGiant() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        byte manaCost = (byte)(baseManaCost - board.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions().size() - board.modelForSide(PlayerSide.WAITING_PLAYER).getMinions().size());
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }

}
