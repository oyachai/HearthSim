package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodKnight extends Minion implements MinionUntargetableBattlecry {

    public BloodKnight() {
        super();
    }

    /**
     * Battlecry: All minions lose Divine Shield.  Gain +3/+3 for each Shield lost
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        for (Minion minion : currentPlayer.getMinions()) {
            if (minion != this && minion.getDivineShield()) {
                minion.setDivineShield(false);
                this.setHealth((byte)(this.getHealth() + 3));
                this.setAttack((byte)(this.getAttack() + 3));
            }
        }
        for (Minion minion : waitingPlayer.getMinions()) {
            if (minion.getDivineShield()) {
                minion.setDivineShield(false);
                this.setHealth((byte)(this.getHealth() + 3));
                this.setAttack((byte)(this.getAttack() + 3));
            }
        }
        return toRet;
    }
}
