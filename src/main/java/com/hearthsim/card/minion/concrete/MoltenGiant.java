package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MoltenGiant extends Minion {

    public MoltenGiant() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        byte manaCost = (byte)(baseManaCost - side.getPlayer(board).getHero().getMaxHealth() + side.getPlayer(board).getHero().getHealth());
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }

}
