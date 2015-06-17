package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DruidOfTheClaw extends Minion {

    private byte baseHealth;

    public DruidOfTheClaw() {
        super();
        // need to force this in case the card loader pulls the wrong Druid of the Claw
        this.setHealth((byte) 4);
        this.setMaxHealth((byte) 4);
        this.baseHealth = (byte) 4;
        this.setTaunt(false);
        this.setCharge(false);
    }

    @Override
    public byte getBaseHealth() {
        return this.baseHealth;
    }

    @Override
    public HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);

        if (toRet != null) {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

            CharacterIndex thisMinionIndex = currentPlayer.getIndexForCharacter(this);

            HearthTreeNode tauntState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
            DruidOfTheClaw newMinion = (DruidOfTheClaw)tauntState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(thisMinionIndex);
            newMinion.setTaunt(true);
            newMinion.setMaxHealth((byte) 6);
            newMinion.setHealth((byte) 6);
            newMinion.baseHealth = ((byte) 6);

            HearthTreeNode chargeState = toRet.addChild(new HearthTreeNode(toRet.data_.deepCopy()));
            newMinion = (DruidOfTheClaw)chargeState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getCharacter(thisMinionIndex);
            newMinion.setCharge(true);
        }
        return toRet;
    }

    @Override
    public DruidOfTheClaw deepCopy() {
        DruidOfTheClaw copy = (DruidOfTheClaw)super.deepCopy();
        copy.baseHealth = this.baseHealth;
        return copy;
    }
}
