package com.hearthsim.card.classic.weapon.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class GladiatorsLongbow extends WeaponCard {
    boolean previousImmuneState = false;

    @Override
    public void beforeAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet) throws HSException {
        super.beforeAttack(targetMinionPlayerSide, targetMinion, toRet);

        BoardModel boardModel = toRet.data_;
        PlayerModel currentPlayer = boardModel.getCurrentPlayer();
        this.previousImmuneState = currentPlayer.getHero().getImmune();
        currentPlayer.getHero().setImmune(true);
    }

    @Override
    public void afterAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet) throws HSException {
        super.afterAttack(targetMinionPlayerSide, targetMinion, toRet);

        BoardModel boardModel = toRet.data_;
        PlayerModel currentPlayer = boardModel.getCurrentPlayer();
        currentPlayer.getHero().setImmune(this.previousImmuneState);
    }
}
