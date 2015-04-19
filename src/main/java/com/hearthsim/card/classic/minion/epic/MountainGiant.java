package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MountainGiant extends Minion {

    public MountainGiant() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        byte manaCost = (byte)(this.getBaseManaCost() - board.modelForSide(side).getHand().size() + 1);
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }
}
