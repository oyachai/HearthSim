package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DragonlingMechanic extends Minion implements MinionUntargetableBattlecry {

    public DragonlingMechanic() {
        super();
    }

    /**
     * Battlecry: Summons a Mechanical Dragonling
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
        if (toRet != null) {
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            if (currentPlayer.getNumMinions() < 7) {
                Minion mdragon = new MechanicalDragonling();
                toRet = mdragon.summonMinion(PlayerSide.CURRENT_PLAYER, this, boardState, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
            }
        }
        return toRet;
    }

}
