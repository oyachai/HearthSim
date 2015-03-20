package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DruidOfTheClaw extends Minion {

    public DruidOfTheClaw() {
        super();
        // need to force this in case the card loader pulls the wrong Druid of the Claw
        this.setHealth((byte) 4);
        this.setMaxHealth((byte) 4);
        this.setTaunt(false);
        this.setCharge(false);
    }

    @Override
    public HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);

        if (toRet != null) {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

            int thisMinionIndex = currentPlayer.getMinions().indexOf(this);

            HearthTreeNode tauntState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
            Minion newMinion = tauntState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions().get(thisMinionIndex);
            newMinion.setTaunt(true);
            newMinion.setMaxHealth((byte) 6);
            newMinion.setHealth((byte) 6);

            HearthTreeNode chargeState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
            newMinion = chargeState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions().get(thisMinionIndex);
            newMinion.setCharge(true);
        }
        return toRet;
    }
}
