package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class DreadCorsair extends Minion {

    public DreadCorsair() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        if (board.modelForSide(side).getHero().getWeapon() == null)
            return this.getBaseManaCost();
        byte manaCost = (byte)(this.getBaseManaCost() - board.modelForSide(side).getHero().getWeapon().getWeaponDamage());
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }

}
