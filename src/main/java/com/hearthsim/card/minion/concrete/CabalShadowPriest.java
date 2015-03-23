package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.battlecry.BattlecryActionTargetable;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CabalShadowPriest extends Minion implements MinionTargetableBattlecry {

    private final static BattlecryActionTargetable battlecryAction = new BattlecryActionTargetable() {
        protected boolean canTargetEnemyMinions() { return true; }

        @Override
        public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
            if(!super.canTargetWithBattlecry(originSide, origin, targetSide, targetCharacterIndex, board)) {
                return false;
            }
            Minion targetCharacter = board.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            return targetCharacter.getAttack() <= 2;
        }

        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
            Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            HearthTreeNode toRet = boardState;
            if (targetMinion.getTotalAttack() <= 2 && toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions().size() < 6) {
                toRet.data_.removeMinion(targetMinion);
                toRet.data_.placeMinion(PlayerSide.CURRENT_PLAYER, targetMinion);
                if (targetMinion.getCharge()) {
                    if (!targetMinion.canAttack()) {
                        targetMinion.hasAttacked(false);
                    }
                } else {
                    targetMinion.hasAttacked(true);
                }
                return boardState;
            } else {
                return null;
            }
        }
    };

    public CabalShadowPriest() {
        super();
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return CabalShadowPriest.battlecryAction.canTargetWithBattlecry(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        return CabalShadowPriest.battlecryAction.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
    }
}
