package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class ClockworkGiant extends Minion {

    public ClockworkGiant() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        byte manaCost = (byte)(baseManaCost - board.modelForSide(side.getOtherPlayer()).getHand().size());
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }
}
