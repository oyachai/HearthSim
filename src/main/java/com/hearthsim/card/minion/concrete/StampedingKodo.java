package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

import java.util.ArrayList;
import java.util.List;

public class StampedingKodo extends Minion implements MinionUntargetableBattlecry {

    public StampedingKodo() {
        super();
    }

    /**
     * Battlecry: Destroy
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        HearthTreeNode toRet = boardState;
        if (singleRealizationOnly) {
            PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            List<Minion> possibleTargets = new ArrayList<>();
            for (Minion minion : waitingPlayer.getMinions()) {
                if (minion.getTotalAttack() <= 2)
                    possibleTargets.add(minion);
            }
            if (possibleTargets.size() > 0) {
                Minion targetMinion = possibleTargets.get((int)(Math.random() * possibleTargets.size()));
                targetMinion.setHealth((byte)-99); //destroyed!
            }
        } else {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);

            int thisMinionIndex = currentPlayer.getMinions().indexOf(this) + 1;
            List<Minion> possibleTargets = new ArrayList<>();
            for (Minion minion : waitingPlayer.getMinions()) {
                if (minion.getTotalAttack() <= 2)
                    possibleTargets.add(minion);
            }
            if (possibleTargets.size() > 0) {
                toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, thisMinionIndex, PlayerSide.CURRENT_PLAYER, minionPlacementIndex));
                PlayerModel targetPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
                for (Minion possibleTarget : possibleTargets) {
                    HearthTreeNode newState = new HearthTreeNode(toRet.data_.deepCopy());
                    Minion targetMinion = newState.data_.modelForSide(PlayerSide.WAITING_PLAYER).getMinions().get(targetPlayer.getMinions().indexOf(possibleTarget));
                    targetMinion.setHealth((byte)-99); //destroyed!
                    newState = BoardStateFactoryBase.handleDeadMinions(newState, singleRealizationOnly);
                    toRet.addChild(newState);
                }
            }
        }
        return toRet;
    }
}
