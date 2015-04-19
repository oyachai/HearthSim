package com.hearthsim.card.classic.weapon.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SwordOfJustice extends WeaponCard {

    @Override
    public void minionSummonedEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
        if (thisMinionPlayerSide == summonedMinionPlayerSide){
            PlayerModel playerModel = boardState.data_.modelForSide(thisMinionPlayerSide);
            playerModel.getHero().getWeapon().setWeaponCharge((byte) (getWeaponCharge() - 1));
            summonedMinion.addAttack((byte) 1);
            summonedMinion.addHealth((byte) 1);
        }
    }
}
