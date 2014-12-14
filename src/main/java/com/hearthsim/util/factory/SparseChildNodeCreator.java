package com.hearthsim.util.factory;

import java.util.ArrayList;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;
import com.hearthsim.util.HearthAction.Verb;
import com.hearthsim.util.tree.HearthTreeNode;

public class SparseChildNodeCreator extends ChildNodeCreatorBase {

	protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	public SparseChildNodeCreator(Deck deckPlayer0, Deck deckPlayer1) {
		super(deckPlayer0, deckPlayer1);
	}

	@Override
	public ArrayList<HearthTreeNode> createPlayCardChildren(HearthTreeNode boardStateNode) throws HSException {
		ArrayList<HearthTreeNode> nodes = new ArrayList<HearthTreeNode>();

		Minion targetMinion = null;
		Card card = null;
		Card copiedCard = null;
		HearthTreeNode newState = null;

		boolean allUsed = true;
		int mana = boardStateNode.data_.getCurrentPlayer().getMana();
		for(int cardIndex = 0; cardIndex < boardStateNode.data_.getNumCards_hand(); ++cardIndex) {
			card = boardStateNode.data_.getCurrentPlayerCardHand(cardIndex);
			if(card == null)
				continue; // Should be impossible

			allUsed = allUsed && card.hasBeenUsed();
			if(card.getManaCost(PlayerSide.CURRENT_PLAYER, boardStateNode) > mana || card.hasBeenUsed()) {
				continue;
			}

			if(card instanceof Minion && !((Minion)card).getPlacementImportant()) {
				// If this card is a minion, then reduce the set of possible minion placement position
				int cardPlacementIndex = this.getMinionPlacementIndex(boardStateNode, (Minion)card);

				// actually place the card now
				targetMinion = boardStateNode.data_.getCurrentPlayerCharacter(cardPlacementIndex);
				if(card.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
					newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
					copiedCard = newState.data_.getCurrentPlayerCardHand(cardIndex);
					newState = copiedCard.useOn(PlayerSide.CURRENT_PLAYER, cardPlacementIndex, newState, deckPlayer0_,
							deckPlayer1_);
					if(newState != null) {
						nodes.add(newState);
					}
				}
			} else {
				// we can use this card! Let's try using it on everything
				for(int targetIndex = 0; targetIndex <= PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode)
						.getNumMinions(); ++targetIndex) {
					targetMinion = boardStateNode.data_.getCurrentPlayerCharacter(targetIndex);

					if(card.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
						newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
						copiedCard = newState.data_.getCurrentPlayerCardHand(cardIndex);
						newState = copiedCard.useOn(PlayerSide.CURRENT_PLAYER, targetIndex, newState,
								deckPlayer0_, deckPlayer1_);
						if(newState != null) {
							nodes.add(newState);
						}
					}
				}

				for(int targetIndex = 0; targetIndex <= PlayerSide.WAITING_PLAYER.getPlayer(boardStateNode)
						.getNumMinions(); ++targetIndex) {
					targetMinion = boardStateNode.data_.getWaitingPlayerCharacter(targetIndex);

					if(card.canBeUsedOn(PlayerSide.WAITING_PLAYER, targetMinion, boardStateNode.data_)) {
						newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
						copiedCard = newState.data_.getCurrentPlayerCardHand(cardIndex);
						newState = copiedCard.useOn(PlayerSide.WAITING_PLAYER, targetIndex, newState,
								deckPlayer0_, deckPlayer1_);
						if(newState != null) {
							nodes.add(newState);
						}
					}
				}
			}
		}

		// If no nodes were created then nothing could be played. If something could be played, we want to explicitly do nothing in its own node.
		if(!nodes.isEmpty()) {
			newState = new HearthTreeNode(boardStateNode.data_.deepCopy());
			newState.setAction(new HearthAction(Verb.DO_NOT_USE_CARD));
			for(Card c : newState.data_.getCurrentPlayerHand()) {
				c.hasBeenUsed(true);
			}
			nodes.add(newState);
		}

		return nodes;
	}

	protected int getMinionPlacementIndex(HearthTreeNode boardStateNode, Minion minion) {

		// If this card is a minion, then reduce the set of possible minion placement position
		int cardPlacementIndex = 0; // by default, place it to the left of everything

		// if there are minions on the board already, place the minion farthest away from the highest attack minion on the board
		if(PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions() > 1) {
			byte thisMinionAttack = minion.getTotalAttack();
			int numMinions = PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions();
			byte maxAttack = -100;
			int maxAttackIndex = 0;
			byte secondMaxAttack = -100;
			int secondMaxAttackIndex = 0;
			for(int midx = 0; midx < numMinions; ++midx) {
				Minion tempMinion = PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getMinions().get(midx);
				if(tempMinion.getTotalAttack() >= maxAttack) {
					secondMaxAttackIndex = maxAttackIndex;
					secondMaxAttack = maxAttack;
					maxAttackIndex = midx;
					maxAttack = tempMinion.getTotalAttack();
				} else if(tempMinion.getTotalAttack() >= secondMaxAttack) {
					secondMaxAttackIndex = midx;
					secondMaxAttack = tempMinion.getTotalAttack();
				}
			}
			if(thisMinionAttack > secondMaxAttack && thisMinionAttack <= maxAttack) {
				// put this minion on the other side of maxAttack minion
				if(secondMaxAttackIndex < maxAttackIndex)
					cardPlacementIndex = 0;
				else
					cardPlacementIndex = numMinions;
			} else {
				// put this minion in between maxAttack and secondMaxAttack
				if(secondMaxAttackIndex < maxAttackIndex) {
					cardPlacementIndex = (maxAttackIndex + secondMaxAttackIndex + 1) / 2 - 1;
				} else {
					cardPlacementIndex = (maxAttackIndex + secondMaxAttackIndex) / 2;
				}
			}
			if(cardPlacementIndex < 0) {
				log.info("blah");
			}
		}

		return cardPlacementIndex;
	}
}
