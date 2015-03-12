package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class LeeroyJenkins extends Minion implements MinionUntargetableBattlecry {

    public LeeroyJenkins() {
        super();
    }

    /**
     * Battlecry: summon two 1/1 whelps for your opponent
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
        for (int index = 0; index < 2; ++index) {
            PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            int numMinions = waitingPlayer.getNumMinions();
            if (numMinions < 7) {
                Minion newMinion = new Whelp();
                Minion placementTarget = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER).getCharacter(numMinions);
                toRet = newMinion.summonMinion(PlayerSide.WAITING_PLAYER, placementTarget, toRet, deckPlayer0, deckPlayer1, false, true);
            }
        }
        return toRet;
    }

}
