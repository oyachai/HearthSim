package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.MinionFilterTargetedBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CrazedAlchemist extends Minion implements MinionTargetableBattlecry {

    /**
     * Battlecry: Swap the Attack and Health of a minion
     */
    private final static MinionFilterTargetedBattlecry filter = new MinionFilterTargetedBattlecry() {
        protected boolean includeEnemyMinions() { return true; }
        protected boolean includeOwnMinions() { return true; }
    };

    private final static CardEffectCharacter battlecryAction = new CardEffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
            Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
            HearthTreeNode toRet = boardState;
            byte newHealth = targetMinion.getTotalAttack();
            byte newAttack = targetMinion.getTotalHealth();
            targetMinion.setAttack(newAttack);
            targetMinion.setHealth(newHealth);
            return toRet;
        }
    };

    public CrazedAlchemist() {
        super();
    }

    @Override
    public boolean canTargetWithBattlecry(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, BoardModel board) {
        return CrazedAlchemist.filter.targetMatches(originSide, origin, targetSide, targetCharacterIndex, board);
    }

    @Override
    public HearthTreeNode useTargetableBattlecry_core(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        return CrazedAlchemist.battlecryAction.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
    }
}
