package com.hearthsim.util.factory;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.tree.HearthTreeNode;

/**
 * A BoardStateFactory in which the minion placement is simplified in order to reduce simulation time
 *
 */
public class SparseBoardStateFactory extends BoardStateFactoryBase {

	/**
	 * Constructor
	 * 
	 * maxThinkTime defaults to 10000 milliseconds (10 seconds)
	 */
	public SparseBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1) {
		this(deckPlayer0, deckPlayer1, 10000);
	}

    /**
     * Constructor
     * 
     * @param deckPlayer0
     * @param deckPlayer1
	 * @param maxThinkTime The maximum amount of time in milliseconds the factory is allowed to spend on generating the simulation tree.
	 */
	public SparseBoardStateFactory(Deck deckPlayer0, Deck deckPlayer1, long maxThinkTime) {
		super(deckPlayer0, deckPlayer1, maxThinkTime);
	}

	@Override
	public HearthTreeNode createCardUseBranches(HearthTreeNode boardStateNode, ArtificialPlayer ai) throws HSException {
		//check to see if all the cards have been used already
		boolean allUsed = true;
		for (final Card card : boardStateNode.data_.getCurrentPlayerHand()) {
			allUsed = allUsed && card.hasBeenUsed();
		}
		
		//the case where I chose not to use any more cards
		if (!allUsed) {
			HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
			for (Card card : newState.data_.getCurrentPlayerHand()) {
				card.hasBeenUsed(true);
			}
			newState = this.doMoves(newState, ai);
			if (newState != null) boardStateNode.addChild(newState);
		}
		
		
		int mana = boardStateNode.data_.getCurrentPlayer().getMana();
		for (int ic = 0; ic < boardStateNode.data_.getNumCards_hand(); ++ic) {
			Card cardToUse = boardStateNode.data_.getCurrentPlayerCardHand(ic);
			if (cardToUse.getMana() <= mana && !cardToUse.hasBeenUsed()) {
				//we can use this card!  Let's try using it on everything
				if (cardToUse instanceof Minion && !((Minion)cardToUse).getPlacementImportant()) {
					//If this card is a minion, then reduce the set of possible minion placement position
					int cardPlacementIndex = 0; //by default, place it to the left of everything
					
					//if there are minions on the board already, place the minion farthest away from the highest attack minion on the board
					if (PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions() > 1) {
						byte thisMinionAttack = ((Minion)cardToUse).getTotalAttack();
						int numMinions = PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions();
						byte maxAttack = -100;
						int maxAttackIndex = 0;
						byte secondMaxAttack = -100;
						int secondMaxAttackIndex = 0;
						for(int midx = 0; midx < numMinions; ++midx) {
							Minion tempMinion = PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getMinions().get(midx);
							if (tempMinion.getTotalAttack() >= maxAttack) {
								secondMaxAttackIndex = maxAttackIndex;
								secondMaxAttack = maxAttack;
								maxAttackIndex = midx;
								maxAttack = tempMinion.getTotalAttack();
							} else if (tempMinion.getTotalAttack() >= secondMaxAttack) {
								secondMaxAttackIndex = midx;
								secondMaxAttack = tempMinion.getTotalAttack();
							}
						}
						if (thisMinionAttack > secondMaxAttack && thisMinionAttack <= maxAttack) {
							//put this minion on the other side of maxAttack minion
							if (secondMaxAttackIndex < maxAttackIndex) 
								cardPlacementIndex = 0;
							else
								cardPlacementIndex = numMinions;
						} else {
							//put this minion in between maxAttack and secondMaxAttack
							if (secondMaxAttackIndex < maxAttackIndex) {
								cardPlacementIndex = (maxAttackIndex + secondMaxAttackIndex + 1) / 2 - 1;
							} else {
								cardPlacementIndex = (maxAttackIndex + secondMaxAttackIndex) / 2;
							}
						}
						if (cardPlacementIndex < 0)
							log.info("blah");
					}
					//actually place the card now
					Minion targetMinion = boardStateNode.data_.getCurrentPlayerCharacter(cardPlacementIndex);
					if (cardToUse.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion, boardStateNode.data_)) {
						HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
						Minion copiedTargetMinion = newState.data_.getCurrentPlayerCharacter(cardPlacementIndex);
						Card card = newState.data_.getCurrentPlayerCardHand(ic);
						newState = card.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
						if (newState != null) {
							newState = this.doMoves(newState, ai);
							if (newState != null) boardStateNode.addChild(newState);
						}
					}
				} else {
					//not a minion card, do the default thorough branching
					for(int i = 0; i <= PlayerSide.CURRENT_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
						Minion targetMinion = boardStateNode.data_.getCurrentPlayerCharacter(i);
						if (cardToUse.canBeUsedOn(PlayerSide.CURRENT_PLAYER, targetMinion,boardStateNode.data_)) {
							HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
							Minion copiedTargetMinion = newState.data_.getCurrentPlayerCharacter(i);
							Card card = newState.data_.getCurrentPlayerCardHand(ic);
							newState = card.getCardAction().useOn(PlayerSide.CURRENT_PLAYER, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
							if (newState != null) {
								newState = this.doMoves(newState, ai);
								if (newState != null) boardStateNode.addChild(newState);
							}
						}
					}
					for(int i = 0; i <= PlayerSide.WAITING_PLAYER.getPlayer(boardStateNode).getNumMinions(); ++i) {
						Minion targetMinion = boardStateNode.data_.getWaitingPlayerCharacter(i);
						if (cardToUse.canBeUsedOn(PlayerSide.WAITING_PLAYER, targetMinion, boardStateNode.data_)) {
							HearthTreeNode newState = new HearthTreeNode((BoardModel)boardStateNode.data_.deepCopy());
							Minion copiedTargetMinion = newState.data_.getWaitingPlayerCharacter(i);
							Card card = newState.data_.getCurrentPlayerCardHand(ic);
							newState = card.getCardAction().useOn(PlayerSide.WAITING_PLAYER, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
							if (newState != null) {
								newState = this.doMoves(newState, ai);
								if (newState != null) boardStateNode.addChild(newState);
							}
						}
					}
				}
			}
		}
		return boardStateNode;
	}
	
}
