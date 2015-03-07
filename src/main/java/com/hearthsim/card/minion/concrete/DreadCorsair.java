package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class DreadCorsair extends Minion {

    public DreadCorsair() {
        super();
    }

    @Override
    public byte getManaCost(PlayerSide side, BoardModel board) {
        if (side.getPlayer(board).getHero().getWeapon() == null)
            return baseManaCost;
        byte manaCost = (byte)(baseManaCost - side.getPlayer(board).getHero().getWeapon().getWeaponDamage());
        if (manaCost < 0)
            manaCost = 0;
        return manaCost;
    }

}
