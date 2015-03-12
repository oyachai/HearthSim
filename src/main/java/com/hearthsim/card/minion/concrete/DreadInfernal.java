package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DreadInfernal extends Minion implements MinionUntargetableBattlecry {

    public DreadInfernal() {
        super();
    }

    /**
     * Battlecry: Deals 1 damage to all characters
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
        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        toRet = currentPlayer.getHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
        for (Minion minion : currentPlayer.getMinions()) {
            if (minion != this)
                toRet = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
        }

        toRet = waitingPlayer.getHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
        for (Minion minion : waitingPlayer.getMinions()) {
            toRet = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
        }

        return toRet;
    }

}
