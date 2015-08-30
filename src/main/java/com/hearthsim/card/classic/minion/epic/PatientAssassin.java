package com.hearthsim.card.classic.minion.epic;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class PatientAssassin extends Minion {

    public PatientAssassin() {
        super();
    }

    @Override
    protected HearthTreeNode attack_core(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {

        if (targetMinion instanceof Hero)
            return super.attack_core(targetMinionPlayerSide, targetMinion, boardState);

        HearthTreeNode toRet = boardState;
        byte origAttack = targetMinion.getTotalAttack(toRet, targetMinionPlayerSide);
        toRet = targetMinion.takeDamageAndNotify((byte) 99, PlayerSide.CURRENT_PLAYER, targetMinionPlayerSide, toRet, false, false);
        toRet = this.takeDamageAndNotify(origAttack, targetMinionPlayerSide, PlayerSide.CURRENT_PLAYER, toRet, false, false);
        if (windFury_ && !hasWindFuryAttacked_)
            hasWindFuryAttacked_ = true;
        else
            hasAttacked_ = true;
        return toRet;
    }
}
