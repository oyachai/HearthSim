package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.battlecry.BattlecryTargetableAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HungryCrab extends Minion implements MinionTargetableBattlecry {

    /**
     * Battlecry: Destroy a murloc and gain +2/+2
     */
    private final static BattlecryTargetableAction battlecryAction = new BattlecryTargetableAction() {
        protected boolean canTargetEnemyMinions() { return true; }
        protected boolean canTargetOwnMinions() { return true; }
        protected MinionTribe tribeFilter() { return MinionTribe.MURLOC; }

        @Override
        public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
            if (targetMinion.getTribe() == MinionTribe.MURLOC) {
                targetMinion.setHealth((byte) -99);
                origin.addAttack((byte) 2);
                origin.addHealth((byte) 2);
                origin.addMaxHealth((byte) 2);
                return boardState;
            } else {
                return null;
            }
        }
    };

    public HungryCrab() {
        super();
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return HungryCrab.battlecryAction.canTargetWithBattlecry(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        return HungryCrab.battlecryAction.useTargetableBattlecry_core(originSide, origin, targetSide, targetMinion, boardState);
    }
}
