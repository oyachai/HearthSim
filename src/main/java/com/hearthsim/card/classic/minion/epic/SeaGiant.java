package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class SeaGiant extends Minion {

    public SeaGiant() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        byte manaCost = (byte)(this.getBaseManaCost() - board.getTotalMinionCount());
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }
}
