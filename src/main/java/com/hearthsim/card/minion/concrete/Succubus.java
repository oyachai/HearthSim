package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.IdentityLinkedList;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;

public class Succubus extends Minion  implements MinionUntargetableBattlecry {

    public Succubus() {
        super();
    }

    /**
     * Battlecry: Discard a random card
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
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        if (singleRealizationOnly) {
            IdentityLinkedList<Card> hand = currentPlayer.getHand();
            if (hand.size() > 0) {
                Card targetCard = hand.get((int)(Math.random() * hand.size()));
                toRet.data_.getCurrentPlayer().getHand().remove(targetCard);
            }
        } else {
            int placementTargetIndex = minionPlacementTarget instanceof Hero ? 0 : currentPlayer.getMinions().indexOf(minionPlacementTarget) + 1;
            int thisMinionIndex = currentPlayer.getMinions().indexOf(this) + 1;
            IdentityLinkedList<Card> hand = currentPlayer.getHand();
            if (hand.size() == 0) {
                return toRet;
            }
            toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, thisMinionIndex, PlayerSide.CURRENT_PLAYER, placementTargetIndex));
            for (int indx0 = 0; indx0 < hand.size(); ++indx0) {
                HearthTreeNode cNode = new HearthTreeNode(toRet.data_.deepCopy());
                cNode.data_.getCurrentPlayer().getHand().remove(cNode.data_.getCurrentPlayer().getHand().get(indx0));
                toRet.addChild(cNode);
            }
        }
        return toRet;
    }
}
