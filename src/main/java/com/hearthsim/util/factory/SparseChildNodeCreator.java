package com.hearthsim.util.factory;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;

public class SparseChildNodeCreator extends ChildNodeCreatorBase {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public SparseChildNodeCreator(Deck deckPlayer0, Deck deckPlayer1) {
        super(deckPlayer0, deckPlayer1);
    }

    @Override
    public ArrayList<HearthTreeNode> createPlayCardChildren(HearthTreeNode boardStateNode) throws HSException {
        ArrayList<HearthTreeNode> nodes = new ArrayList<>();

        Minion targetMinion;
        Card card;
        Card copiedCard;
        HearthTreeNode newState;

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = boardStateNode.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        boolean allUsed = true;
        int mana = boardStateNode.data_.getCurrentPlayer().getMana();
        for (int cardIndex = 0; cardIndex < boardStateNode.data_.getCurrentPlayer().getHand().size(); ++cardIndex) {
            card = boardStateNode.data_.getCurrentPlayer().getHand().get(cardIndex);
            if (card == null)
                continue; // Should be impossible

            allUsed = allUsed && card.hasBeenUsed();
            if (card.getManaCost(PlayerSide.CURRENT_PLAYER, boardStateNode.data_) > mana || card.hasBeenUsed()) {
                continue;
            }

            if (card instanceof Minion && !((Minion)card).getPlacementImportant()) {
                // If this card is a minion, then reduce the set of possible minion placement position
                CharacterIndex cardPlacementIndex = this.getMinionPlacementIndex(boardStateNode, (Minion)card);

                // actually place the card now
                targetMinion = boardStateNode.data_.getCurrentPlayer().getCharacter(cardPlacementIndex);
                if (card.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
                    newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                    copiedCard = newState.data_.getCurrentPlayer().getHand().get(cardIndex);
                    newState = copiedCard.useOn(PlayerSide.CURRENT_PLAYER, cardPlacementIndex, newState);
                    if (newState != null) {
                        nodes.add(newState);
                    }
                }
            } else {
                // we can use this card! Let's try using it on everything
                for (int tIndex = 0; tIndex <= currentPlayer.getNumMinions(); ++tIndex) {
                    CharacterIndex targetIndex = CharacterIndex.fromInteger(tIndex);
                    targetMinion = boardStateNode.data_.getCurrentPlayer().getCharacter(targetIndex);

                    if (card.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
                        newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                        copiedCard = newState.data_.getCurrentPlayer().getHand().get(cardIndex);
                        newState = copiedCard.useOn(PlayerSide.CURRENT_PLAYER, targetIndex, newState);
                        if (newState != null) {
                            nodes.add(newState);
                        }
                    }
                }

                for (int tIndex = 0; tIndex <= waitingPlayer.getNumMinions(); ++tIndex) {
                    CharacterIndex targetIndex = CharacterIndex.fromInteger(tIndex);
                    targetMinion = boardStateNode.data_.getWaitingPlayer().getCharacter(targetIndex);

                    if (card.canBeUsedOn(PlayerSide.WAITING_PLAYER, targetMinion, boardStateNode.data_)) {
                        newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                        copiedCard = newState.data_.getCurrentPlayer().getHand().get(cardIndex);
                        newState = copiedCard.useOn(PlayerSide.WAITING_PLAYER, targetIndex, newState);
                        if (newState != null) {
                            nodes.add(newState);
                        }
                    }
                }
            }
        }

        // If no nodes were created then nothing could be played. If something could be played, we want to explicitly do nothing in its own node.
        if (!nodes.isEmpty()) {
            newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
            newState.setAction(new HearthAction(Verb.DO_NOT_USE_CARD));
            for (Card c : newState.data_.getCurrentPlayer().getHand()) {
                c.hasBeenUsed(true);
            }
            nodes.add(newState);
        }

        return nodes;
    }

    protected CharacterIndex getMinionPlacementIndex(HearthTreeNode boardStateNode, Minion minion) {

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        // If this card is a minion, then reduce the set of possible minion placement position
        int cardPlacementIndex = 0; // by default, place it to the left of everything

        // if there are minions on the board already, place the minion farthest away from the highest attack minion on the board
        if (currentPlayer.getNumMinions() > 1) {
            byte thisMinionAttack = minion.getTotalAttack(boardStateNode, PlayerSide.CURRENT_PLAYER);
            int numMinions = currentPlayer.getNumMinions();
            byte maxAttack = -100;
            int maxAttackIndex = 0;
            byte secondMaxAttack = -100;
            int secondMaxAttackIndex = 0;
            for (int midx = 0; midx < numMinions; ++midx) {
                Minion tempMinion = currentPlayer.getCharacter(CharacterIndex.fromInteger(midx + 1));
                if (tempMinion.getTotalAttack(boardStateNode, PlayerSide.CURRENT_PLAYER) >= maxAttack) {
                    secondMaxAttackIndex = maxAttackIndex;
                    secondMaxAttack = maxAttack;
                    maxAttackIndex = midx;
                    maxAttack = tempMinion.getTotalAttack(boardStateNode, PlayerSide.CURRENT_PLAYER);
                } else if (tempMinion.getTotalAttack(boardStateNode, PlayerSide.CURRENT_PLAYER) >= secondMaxAttack) {
                    secondMaxAttackIndex = midx;
                    secondMaxAttack = tempMinion.getTotalAttack(boardStateNode, PlayerSide.CURRENT_PLAYER);
                }
            }
            if (thisMinionAttack > secondMaxAttack && thisMinionAttack <= maxAttack) {
                // put this minion on the other side of maxAttack minion
                if (secondMaxAttackIndex < maxAttackIndex)
                    cardPlacementIndex = 0;
                else
                    cardPlacementIndex = numMinions;
            } else {
                // put this minion in between maxAttack and secondMaxAttack
                if (secondMaxAttackIndex < maxAttackIndex) {
                    cardPlacementIndex = (maxAttackIndex + secondMaxAttackIndex + 1) / 2 - 1;
                } else {
                    cardPlacementIndex = (maxAttackIndex + secondMaxAttackIndex) / 2;
                }
            }
            if (cardPlacementIndex < 0) {
                log.info("blah");
            }
        }

        return CharacterIndex.fromInteger(cardPlacementIndex);
    }
}
