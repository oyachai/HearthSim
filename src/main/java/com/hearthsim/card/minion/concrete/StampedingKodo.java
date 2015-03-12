package com.hearthsim.card.minion.concrete;

import java.util.ArrayList;
import java.util.List;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class StampedingKodo extends Minion implements MinionUntargetableBattlecry {

    public StampedingKodo() {
        super();
    }

    /**
     * Battlecry: Destroy
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        if (singleRealizationOnly) {
            PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            if (toRet != null) {
                List<Minion> possibleTargets = new ArrayList<Minion>();
                for (Minion minion : waitingPlayer.getMinions()) {
                    if (minion.getTotalAttack() <= 2)
                        possibleTargets.add(minion);
                }
                if (possibleTargets.size() > 0) {
                    Minion targetMinion = possibleTargets.get((int)(Math.random() * possibleTargets.size()));
                    targetMinion.setHealth((byte)-99); //destroyed!
                }
            }
        } else {
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);

            int placementTargetIndex = minionPlacementTarget instanceof Hero ? 0 : currentPlayer.getMinions().indexOf(minionPlacementTarget) + 1;
            int thisMinionIndex = currentPlayer.getMinions().indexOf(this) + 1;
            List<Minion> possibleTargets = new ArrayList<Minion>();
            for (Minion minion : waitingPlayer.getMinions()) {
                if (minion.getTotalAttack() <= 2)
                    possibleTargets.add(minion);
            }
            if (possibleTargets.size() > 0) {
                toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, thisMinionIndex, PlayerSide.CURRENT_PLAYER, placementTargetIndex));
                PlayerModel targetPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
                for (Minion possibleTarget : possibleTargets) {
                    HearthTreeNode newState = new HearthTreeNode(toRet.data_.deepCopy());
                    Minion targetMinion = newState.data_.modelForSide(PlayerSide.WAITING_PLAYER).getMinions().get(targetPlayer.getMinions().indexOf(possibleTarget));
                    targetMinion.setHealth((byte)-99); //destroyed!
                    newState = BoardStateFactoryBase.handleDeadMinions(newState, deckPlayer0, deckPlayer1, singleRealizationOnly);
                    toRet.addChild(newState);
                }
            }
        }
        return toRet;
    }
}
