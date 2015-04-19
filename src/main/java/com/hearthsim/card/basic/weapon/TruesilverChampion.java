package com.hearthsim.card.basic.weapon;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TruesilverChampion extends WeaponCard {

    @Override
    public void beforeAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet) throws HSException {
        super.beforeAttack(targetMinionPlayerSide, targetMinion, toRet);

        BoardModel boardModel = toRet.data_;
        PlayerModel currentPlayer = boardModel.getCurrentPlayer();
        currentPlayer.getHero().takeHealAndNotify((byte) 2, targetMinionPlayerSide, toRet);
    }
}
