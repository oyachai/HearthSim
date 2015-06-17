package com.hearthsim.card.basic.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
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
            CharacterIndex minionPlacementIndex,
            HearthTreeNode boardState
        ) {
        HearthTreeNode toRet = boardState;
        PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        CharacterIndex thisMinionIndex = currentPlayer.getIndexForCharacter(this);
        IdentityLinkedList<Card> hand = currentPlayer.getHand();
        if (hand.size() == 0) {
            return toRet;
        }
        toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, thisMinionIndex.getInt(), PlayerSide.CURRENT_PLAYER, minionPlacementIndex));
        for (int indx0 = 0; indx0 < hand.size(); ++indx0) {
            HearthTreeNode cNode = new HearthTreeNode(toRet.data_.deepCopy());
            cNode.data_.getCurrentPlayer().getHand().remove(cNode.data_.getCurrentPlayer().getHand().get(indx0));
            toRet.addChild(cNode);
        }

        return toRet;
    }
}
