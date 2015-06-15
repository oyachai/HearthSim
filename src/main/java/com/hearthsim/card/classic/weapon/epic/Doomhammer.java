package com.hearthsim.card.classic.weapon.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Doomhammer extends WeaponCard {

    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);
        if (toRet != null) {
            toRet.data_.modelForSide(side).getHero().setWindfury(true);
        }
        return toRet;
    }
}
