package com.hearthsim.event.deathrattle;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleCardDrawAction extends DeathrattleAction {

    private final int numCards_;
    private final boolean targetEnemyHero_;

    public DeathrattleCardDrawAction(int numCards) {
        numCards_ = numCards;
        targetEnemyHero_ = false;
    }

    public DeathrattleCardDrawAction(int numCards, boolean targetEnemyHero) {
        numCards_ = numCards;
        targetEnemyHero_ = targetEnemyHero;
    }

    @Override
    public HearthTreeNode performAction(CharacterIndex originIndex,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState) {
        HearthTreeNode toRet = super.performAction(originIndex, playerSide, boardState);
        if (toRet == null)
            return null;
        PlayerSide targetSide = targetEnemyHero_ ? playerSide.getOtherPlayer() : playerSide;
        if (targetSide == PlayerSide.CURRENT_PLAYER) {
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode) toRet).addNumCardsToDraw(numCards_);
            } else {
                toRet = new CardDrawNode(toRet, numCards_); //draw one card
            }
        } else {
            //This minion is an enemy minion.  Let's draw a card for the enemy.  No need to use a StopNode for enemy card draws.
            toRet.data_.drawCardFromWaitingPlayerDeck(numCards_);
        }
        return toRet;
    }


}
