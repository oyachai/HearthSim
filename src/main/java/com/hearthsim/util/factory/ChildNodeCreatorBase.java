package com.hearthsim.util.factory;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.ArrayList;

public class ChildNodeCreatorBase implements ChildNodeCreator {

    /**
     * Constructor
     * maxThinkTime defaults to 10000 milliseconds (10 seconds)
     */
    public ChildNodeCreatorBase(Deck deckPlayer0, Deck deckPlayer1) {
    }

    @Override
    public ArrayList<HearthTreeNode> createAttackChildren(HearthTreeNode boardStateNode) throws HSException {
        ArrayList<HearthTreeNode> nodes = new ArrayList<>();

        ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions();

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

        HearthTreeNode newState;
        Minion tempMinion;

        // attack with characters
        for (int attackerIndex = 0; attackerIndex < currentPlayer
                .getNumCharacters(); ++attackerIndex) {
            if (!currentPlayer.getCharacter(CharacterIndex.fromInteger(attackerIndex)).canAttack(boardStateNode.data_, PlayerSide.CURRENT_PLAYER)) {
                continue;
            }

            for (final Integer integer : attackable) {
                CharacterIndex targetIndex = CharacterIndex.fromInteger(integer);
                newState = new HearthTreeNode(boardStateNode.data_.deepCopy());

                tempMinion = newState.data_.getCurrentPlayer().getCharacter(CharacterIndex.fromInteger(attackerIndex));

                newState = tempMinion.attack(PlayerSide.WAITING_PLAYER, targetIndex, newState);

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
        ArrayList<HearthTreeNode> nodes = new ArrayList<>();

        Minion targetMinion;
        Card card;
        Card copiedCard;
        HearthTreeNode newState;

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = boardStateNode.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        int mana = boardStateNode.data_.getCurrentPlayer().getMana();
        for (int cardIndex = 0; cardIndex < boardStateNode.data_.getCurrentPlayer().getHand().size(); ++cardIndex) {
            card = boardStateNode.data_.getCurrentPlayer().getHand().get(cardIndex);
            if (card.getManaCost(PlayerSide.CURRENT_PLAYER, boardStateNode.data_) <= mana && !card.hasBeenUsed()) {

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

    @Override
    public ArrayList<HearthTreeNode> createHeroAbilityChildren(HearthTreeNode boardStateNode) throws HSException {
        ArrayList<HearthTreeNode> nodes = new ArrayList<>();

        Hero player = boardStateNode.data_.getCurrentPlayer().getHero();
        if (player.hasBeenUsed()) {
            return nodes;
        }

        PlayerModel currentPlayer = boardStateNode.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = boardStateNode.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        HearthTreeNode newState;

        // Case0: Decided to use the hero ability -- Use it on everything!
        for (int i = 0; i <= currentPlayer.getNumMinions(); ++i) {
            CharacterIndex targetIndex = CharacterIndex.fromInteger(i);
            if (player.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetIndex, boardStateNode.data_)) {
                newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                newState = newState.data_.getCurrentPlayer().getHero().useHeroAbility(PlayerSide.CURRENT_PLAYER, targetIndex, newState);

                if (newState != null) {
                    nodes.add(newState);
                }
            }
        }

        for (int i = 0; i <= waitingPlayer.getNumMinions(); ++i) {
            CharacterIndex targetIndex = CharacterIndex.fromInteger(i);
            if (player.canBeUsedOn(PlayerSide.WAITING_PLAYER, targetIndex, boardStateNode.data_)) {
                newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
                newState = newState.data_.getCurrentPlayer().getHero().useHeroAbility(PlayerSide.WAITING_PLAYER, targetIndex, newState);

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
