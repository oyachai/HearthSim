package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class AcidicSwampOoze extends Minion implements MinionUntargetableBattlecry {

    public AcidicSwampOoze() {
        super();
    }

    /**
     * Battlecry: Destroy your opponent's weapon
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
        Minion minionPlacementTarget,
        HearthTreeNode boardState,
        Deck deckPlayer0,
        Deck deckPlayer1,
        boolean singleRealizationOnly
    ) throws HSException {

        DeathrattleAction action = boardState.data_.getWaitingPlayerHero().destroyWeapon();
        if (action != null) {
            boardState = action.performAction(null, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
        }
        return boardState;
    }

}
