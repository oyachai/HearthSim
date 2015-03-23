package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.battlecry.BattlecryTargetableAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DarkIronDwarf extends Minion implements MinionTargetableBattlecry {

    /**
     * Battlecry: Give a minion +2 attack this turn
     */
    private final static BattlecryTargetableAction battlecryAction = new BattlecryTargetableAction() {
        protected boolean canTargetEnemyMinions() { return true; }
        protected boolean canTargetOwnMinions() { return true; }

        @Override
        public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
            targetMinion.setExtraAttackUntilTurnEnd(((byte)(targetMinion.getExtraAttackUntilTurnEnd() + 2)));
            return boardState;
        }
    };

    public DarkIronDwarf() {
        super();
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return DarkIronDwarf.battlecryAction.canTargetWithBattlecry(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        return DarkIronDwarf.battlecryAction.useTargetableBattlecry_core(originSide, origin, targetSide, targetMinion, boardState);
    }
}
