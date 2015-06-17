package com.hearthsim.card.classic.minion.rare;

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

public class Doomguard extends Minion implements MinionUntargetableBattlecry {

    public Doomguard() {
        super();
    }

    /**
     * Battlecry: Discard 2 random cards
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            CharacterIndex minionPlacementIndex,
            HearthTreeNode boardState
        ) {
        HearthTreeNode toRet = boardState;

        PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        CharacterIndex thisMinionIndex = currentPlayer.getIndexForCharacter(this);
        IdentityLinkedList<Card> hand = currentPlayer.getHand();
        if (hand.size() == 0) {
            return toRet;
        }
        toRet = new RandomEffectNode(boardState, new HearthAction(HearthAction.Verb.UNTARGETABLE_BATTLECRY, PlayerSide.CURRENT_PLAYER, thisMinionIndex.getInt(), PlayerSide.CURRENT_PLAYER, minionPlacementIndex));
        for (int indx0 = 0; indx0 < hand.size(); ++indx0) {
            if (hand.size() > 1) {
                for (int indx1 = indx0+1; indx1 < hand.size(); ++indx1) {
                    HearthTreeNode cNode = new HearthTreeNode(toRet.data_.deepCopy());
                    cNode.data_.getCurrentPlayer().getHand().remove(cNode.data_.getCurrentPlayer().getHand().get(indx1));
                    cNode.data_.getCurrentPlayer().getHand().remove(cNode.data_.getCurrentPlayer().getHand().get(indx0)); //indx1 > indx0
                    toRet.addChild(cNode);
                }
            } else {
                HearthTreeNode cNode = new HearthTreeNode(toRet.data_.deepCopy());
                cNode.data_.getCurrentPlayer().getHand().remove(cNode.data_.getCurrentPlayer().getHand().get(indx0));
                toRet.addChild(cNode);
            }
        }

        return toRet;
    }
}
