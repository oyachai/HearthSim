package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MurlocTidehunter extends Minion implements MinionUntargetableBattlecry {

    public MurlocTidehunter() {
        super();
    }

    /**
     * Battlecry: Summon a Murloc Scout
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
            PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            if (currentPlayer.getNumMinions() < 7) {
                Minion mdragon = new MurlocScout();
                toRet = mdragon.summonMinion(PlayerSide.CURRENT_PLAYER, this, boardState, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
            }
        }
        return toRet;
    }

}
