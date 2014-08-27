package com.hearthsim.util.boardstate;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
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
		for (final Card card : boardStateNode.data_.getCards_hand_p0()) {
			allUsed = allUsed && card.hasBeenUsed();
		}
		
		//the case where I chose not to use any more cards
		if (!allUsed) {
			HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
			for (Card card : newState.data_.getCards_hand_p0()) {
				card.hasBeenUsed(true);
			}
			newState = this.doMoves(newState, ai);
			if (newState != null) boardStateNode.addChild(newState);
		}
		
		
		int mana = boardStateNode.data_.getMana_p0();
		for (int ic = 0; ic < boardStateNode.data_.getNumCards_hand(); ++ic) {
			Card cardToUse = boardStateNode.data_.getCard_hand_p0(ic);
			if (cardToUse.getMana() <= mana && !cardToUse.hasBeenUsed()) {
				//we can use this card!  Let's try using it on everything
				if (cardToUse instanceof Minion && !((Minion)cardToUse).getPlacementImportant()) {
					//If this card is a minion, then reduce the set of possible minion placement position
					int cardPlacementIndex = 0; //by default, place it to the left of everything
					
					//if there are minions on the board already, place the minion farthest away from the highest attack minion on the board
					if (boardStateNode.data_.getNumMinions_p0() > 1) {
						byte thisMinionAttack = ((Minion)cardToUse).getTotalAttack();
						int numMinions = boardStateNode.data_.getNumMinions_p0();
						byte maxAttack = -100;
						int maxAttackIndex = 0;
						byte secondMaxAttack = -100;
						int secondMaxAttackIndex = 0;
						for(int midx = 0; midx < numMinions; ++midx) {
							Minion tempMinion = boardStateNode.data_.getMinion_p0(midx);
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
							System.out.println("blah");
					}
					//actually place the card now
					Minion targetMinion = boardStateNode.data_.getCharacter_p0(cardPlacementIndex);
					if (cardToUse.canBeUsedOn(0, targetMinion)) {
						HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
						Minion copiedTargetMinion = newState.data_.getCharacter_p0(cardPlacementIndex);
						Card card = newState.data_.getCard_hand_p0(ic);
						newState = card.useOn(0, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
						if (newState != null) {
							newState = this.doMoves(newState, ai);
							if (newState != null) boardStateNode.addChild(newState);
						}
					}
				} else {
					//not a minion card, do the default thorough branching
					for(int i = 0; i <= boardStateNode.data_.getNumMinions_p0(); ++i) {
						Minion targetMinion = boardStateNode.data_.getCharacter_p0(i);
						if (cardToUse.canBeUsedOn(0, targetMinion)) {
							HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
							Minion copiedTargetMinion = newState.data_.getCharacter_p0(i);
							Card card = newState.data_.getCard_hand_p0(ic);
							newState = card.useOn(0, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
							if (newState != null) {
								newState = this.doMoves(newState, ai);
								if (newState != null) boardStateNode.addChild(newState);
							}
						}
					}
					for(int i = 0; i <= boardStateNode.data_.getNumMinions_p1(); ++i) {
						Minion targetMinion = boardStateNode.data_.getCharacter_p1(i);
						if (cardToUse.canBeUsedOn(1, targetMinion)) {
							HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
							Minion copiedTargetMinion = newState.data_.getCharacter_p1(i);
							Card card = newState.data_.getCard_hand_p0(ic);
							newState = card.useOn(1, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_, false);
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
