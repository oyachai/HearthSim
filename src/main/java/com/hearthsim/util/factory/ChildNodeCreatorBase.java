package com.hearthsim.util.factory;

import java.util.ArrayList;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.tree.HearthTreeNode;

public class ChildNodeCreatorBase implements ChildNodeCreator {

    protected final Deck deckPlayer0_;
    protected final Deck deckPlayer1_;

    /**
     * Constructor
     * maxThinkTime defaults to 10000 milliseconds (10 seconds)
     */
    public ChildNodeCreatorBase(Deck deckPlayer0, Deck deckPlayer1) {
        deckPlayer0_ = deckPlayer1;
        deckPlayer1_ = deckPlayer1;
    }

    @Override
    public ArrayList<HearthTreeNode> createAttackChildren(HearthTreeNode boardStateNode) throws HSException {
        ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();

        ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions();

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        HearthTreeNode newState = null;
        Minion targetMinion = null;
        Minion tempMinion = null;

        // attack with characters
        for (int attackerIndex = 0; attackerIndex < currentPlayer
                .getNumCharacters(); ++attackerIndex) {
            if (!currentPlayer.getCharacter(attackerIndex).canAttack()) {
                continue;
            }

            for (final Integer integer : attackable) {
                int targetIndex = integer.intValue();
                newState = new HearthTreeNode(boardStateNode.data_.deepCopy());

                targetMinion = newState.data_.getWaitingPlayer().getCharacter(targetIndex);
                tempMinion = newState.data_.getCurrentPlayer().getCharacter(attackerIndex);

                newState = tempMinion.attack(PlayerSide.WAITING_PLAYER, targetMinion, newState, deckPlayer0_,
                        deckPlayer1_, false);

                if (newState != null) {
                    nodes.add(newState);
                }
            }
        }

        // If no nodes were created then nothing could attack. If something could attack, we want to explicitly do nothing in its own node.
        if (!nodes.isEmpty()) {
            newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
            newState.setAction(new HearthAction(Verb.DO_NOT_ATTACK));
            for (Minion minion : newState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getMinions()) {
                minion.hasAttacked(true);
            }
            newState.data_.getCurrentPlayer().getHero().hasAttacked(true);
            nodes.add(newState);
        }

        return nodes;
    }

    @Override
    public ArrayList<HearthTreeNode> createPlayCardChildren(HearthTreeNode boardStateNode) throws HSException {
        ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();

        Minion targetMinion = null;
        Card card = null;
        Card copiedCard = null;
        HearthTreeNode newState = null;

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = boardStateNode.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        int mana = boardStateNode.data_.getCurrentPlayer().getMana();
        for (int cardIndex = 0; cardIndex < boardStateNode.data_.getNumCards_hand(); ++cardIndex) {
            card = boardStateNode.data_.getCurrentPlayerCardHand(cardIndex);
            if (card.getManaCost(PlayerSide.CURRENT_PLAYER, boardStateNode.data_) <= mana && !card.hasBeenUsed()) {

                // we can use this card! Let's try using it on everything
                for (int targetIndex = 0; targetIndex <= currentPlayer.getNumMinions(); ++targetIndex) {
                    targetMinion = boardStateNode.data_.getCurrentPlayer().getCharacter(targetIndex);

                    if (card.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
                        newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                        copiedCard = newState.data_.getCurrentPlayerCardHand(cardIndex);
                        newState = copiedCard.useOn(PlayerSide.CURRENT_PLAYER, targetIndex, newState,
                                deckPlayer0_, deckPlayer1_);
                        if (newState != null) {
                            nodes.add(newState);
                        }
                    }
                }

                for (int targetIndex = 0; targetIndex <= waitingPlayer.getNumMinions(); ++targetIndex) {
                    targetMinion = boardStateNode.data_.getWaitingPlayer().getCharacter(targetIndex);

                    if (card.canBeUsedOn(PlayerSide.WAITING_PLAYER, targetMinion, boardStateNode.data_)) {
                        newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                        copiedCard = newState.data_.getCurrentPlayerCardHand(cardIndex);
                        newState = copiedCard.useOn(PlayerSide.WAITING_PLAYER, targetIndex, newState,
                                deckPlayer0_, deckPlayer1_);
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

    @Override
    public ArrayList<HearthTreeNode> createHeroAbilityChildren(HearthTreeNode boardStateNode) throws HSException {
        ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();

        Hero player = boardStateNode.data_.getCurrentPlayer().getHero();
        if (player.hasBeenUsed()) {
            return nodes;
        }

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = boardStateNode.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        HearthTreeNode newState = null;
        Minion copiedTargetMinion = null;

        // Case0: Decided to use the hero ability -- Use it on everything!
        for (int i = 0; i <= currentPlayer.getNumMinions(); ++i) {
            Minion target = boardStateNode.data_.getCurrentPlayer().getCharacter(i);

            if (player.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, boardStateNode.data_)) {

                newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                copiedTargetMinion = newState.data_.getCurrentPlayer().getCharacter(i);

                newState = newState.data_.getCurrentPlayer().getHero().useHeroAbility(PlayerSide.CURRENT_PLAYER,
                        copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);

                if (newState != null) {
                    nodes.add(newState);
                }
            }
        }

        for (int i = 0; i <= waitingPlayer.getNumMinions(); ++i) {
            Minion target = boardStateNode.data_.getWaitingPlayer().getCharacter(i);
            if (player.canBeUsedOn(PlayerSide.WAITING_PLAYER, target, boardStateNode.data_)) {

                newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                copiedTargetMinion = newState.data_.getWaitingPlayer().getCharacter(i);

                newState = newState.data_.getCurrentPlayer().getHero().useHeroAbility(PlayerSide.WAITING_PLAYER,
                        copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);

                if (newState != null) {
                    nodes.add(newState);
                }
            }
        }

        // Don't need to check hasBeenUsed here because we checked it above
        if (!nodes.isEmpty()) {
            // Case1: Decided not to use the hero ability
            newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
            newState.setAction(new HearthAction(Verb.DO_NOT_USE_HEROPOWER));
            newState.data_.getCurrentPlayer().getHero().hasBeenUsed(true);
            nodes.add(newState);
        }

        return nodes;
    }

}
