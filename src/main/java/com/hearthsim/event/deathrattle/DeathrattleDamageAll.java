package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDamageAll extends DeathrattleAction {

    private final byte damage_;

    public DeathrattleDamageAll(byte damage) {
        damage_ = damage;
    }

    @Override
    public HearthTreeNode performAction(
        Card origin,
        PlayerSide playerSide,
        HearthTreeNode boardState,
        Deck deckPlayer0,
        Deck deckPlayer1)
        throws HSException {
        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, deckPlayer0, deckPlayer1);
        if (toRet != null) {
            toRet = toRet.data_.getCurrentPlayerHero().takeDamage(damage_, playerSide, PlayerSide.CURRENT_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
            for (Minion aMinion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                toRet = aMinion.takeDamage(damage_, playerSide, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
            }
            toRet = toRet.data_.getWaitingPlayerHero().takeDamage(damage_, playerSide, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
            for (Minion aMinion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                toRet = aMinion.takeDamage(damage_, playerSide, PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
            }
        }
        return toRet;
    }

}
