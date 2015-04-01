package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleCardDrawAction extends DeathrattleAction {

    private final int numCards_;

    public DeathrattleCardDrawAction(int numCards) {
        numCards_ = numCards;
    }

    @Override
    public HearthTreeNode performAction(Card origin,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState,
                                        boolean singleRealizationOnly) {
        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, singleRealizationOnly);
        if (playerSide == PlayerSide.CURRENT_PLAYER) {
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
