package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Onyxia extends Minion implements MinionUntargetableBattlecry  {

    public Onyxia() {
        super();
    }

    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
        while (!currentPlayer.isBoardFull()) {
            Minion placementTarget = null;
            if (currentPlayer.getMinions().size() % 2 == 0) {
                placementTarget = toRet.data_.getCurrentPlayer().getCharacter(currentPlayer.getMinions().indexOf(this));
            } else {
                placementTarget = this;
            }
            toRet = new Whelp().summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
        }
        return toRet;
    }

}
