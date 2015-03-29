package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.card.spellcard.concrete.Bananas;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class KingMukla extends Minion implements MinionUntargetableBattlecry {

    public KingMukla() {
        super();
    }

    /**
     * Battlecry: Give your opponent 2 bananas
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            int minionPlacementIndex,
            HearthTreeNode boardState,
            boolean singleRealizationOnly
        ) {
        PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        for (int index = 0; index < 2; ++index) {
            int numCards = waitingPlayer.getHand().size();
            if (numCards < 10) {
                waitingPlayer.placeCardHand(new Bananas());
            }
        }
        return boardState;
    }
}
