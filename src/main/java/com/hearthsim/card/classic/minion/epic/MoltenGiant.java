package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class MoltenGiant extends Minion {

    public MoltenGiant() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        Hero hero = board.modelForSide(side).getHero();
        byte manaCost = (byte)(this.getBaseManaCost() - hero.getMaxHealth() + hero.getHealth());
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }
}
