package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.battlecry.BattlecryActionTargetable;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Houndmaster extends Minion implements MinionTargetableBattlecry {

    /**
     * Battlecry: Give a friendly beast +2/+2 and Taunt
     */
    private final static BattlecryActionTargetable battlecryAction = new BattlecryActionTargetable() {
        protected boolean canTargetOwnMinions() { return true; }
        protected MinionTribe tribeFilter() { return MinionTribe.BEAST; }

        @Override
        public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
            Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            HearthTreeNode toRet = boardState;
            targetMinion.setAttack((byte) (targetMinion.getAttack() + 2));
            targetMinion.setHealth((byte) (targetMinion.getHealth() + 2));
            targetMinion.setTaunt(true);
            return toRet;
        }
    };

    public Houndmaster() {
        super();
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return Houndmaster.battlecryAction.canTargetWithBattlecry(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        return Houndmaster.battlecryAction.useTargetableBattlecry_core(originSide, origin, targetSide, targetCharacterIndex, boardState);
    }
}
