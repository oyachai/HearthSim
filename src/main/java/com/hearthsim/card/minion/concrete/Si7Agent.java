package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.battlecry.BattlecryActionTargetable;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * Created by oyachai on 2/1/15.
 */
public class Si7Agent extends Minion implements MinionTargetableBattlecry {

    private final static BattlecryActionTargetable battlecryAction = new BattlecryActionTargetable() {
        protected boolean canTargetEnemyHero() { return true; }
        protected boolean canTargetEnemyMinions() { return true; }
        protected boolean canTargetOwnHero() { return true; }
        protected boolean canTargetOwnMinions() { return true; }

        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
            Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            HearthTreeNode toRet = boardState;
            if (toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER).isComboEnabled()) {
                toRet = targetMinion.takeDamageAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, targetSide, toRet, false, true);
                return toRet;
            } else {
                return null;
            }
        }
    };

    public Si7Agent() {
        super();
    }

    @Override
    public boolean hasBattlecry() {
        return false;
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return Si7Agent.battlecryAction.canTargetWithBattlecry(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        return Si7Agent.battlecryAction.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
    }
}
