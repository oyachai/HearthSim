package com.hearthsim.card.classic.weapon.epic;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Gorehowl extends WeaponCard {
    boolean previousImmuneState = false;

    @Override
    public void beforeAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet) throws HSException {
        super.beforeAttack(targetMinionPlayerSide, targetMinion, toRet);

        if (!(targetMinion instanceof Hero)) {
            this.previousImmuneState = this.isImmune();
            this.setImmune(true);
        }
    }

    @Override
    public void afterAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet) throws HSException {
        super.afterAttack(targetMinionPlayerSide, targetMinion, toRet);

        if (!(targetMinion instanceof Hero)) {
            this.setImmune(previousImmuneState);
            this.setWeaponDamage((byte) (this.getWeaponDamage() - 1));
            if (this.getWeaponDamage() <= 0) {
                this.setWeaponCharge((byte) 0);
            }
        }
    }
}
