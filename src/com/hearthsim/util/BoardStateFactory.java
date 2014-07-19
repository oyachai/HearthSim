package com.hearthsim.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.StopNode;

public class BoardStateFactory {

	final Deck deck_;
	
	boolean lethal_;
	boolean timedOut_;
	public final long maxTime_;
	
	long startTime_;
	long curTime_;
	
	double curScore_;
	
	/**
	 * Constructor
	 * 
	 * maxThinkTime defaults to 20000 milliseconds (20 seconds)
	 */
	public BoardStateFactory(Deck deck) {
		this(deck, 20000);
	}
	
	/**
	 * Constructor
	 * 
	 * @param maxThinkTime The maximum amount of time in milliseconds the factory is allowed to spend on generating the simulation tree.
	 */
	public BoardStateFactory(Deck deck, long maxThinkTime) {
		deck_ = deck;
		lethal_ = false;
		startTime_ = System.currentTimeMillis();
		curScore_ = -1.e200;
		maxTime_ = maxThinkTime;
		timedOut_ = false;
	}
	
	public boolean didTimeOut() {
		return timedOut_;
	}

	/**
	 * Recursively generate all possible moves
	 * 
	 * This function recursively generates all possible moves that can be done starting from a given BoardState.
	 * While generating the moves, it applies the scoring function to each BoardState generated, and it will only keep the
	 * highest scoring branch.
	 * 
	 * The results are stored in a tree structure and returned as a tree of BoardState class.
	 * 
	 * @param boardStateNode The initial BoardState wrapped in a HearthTreeNode.
	 * @param scoreFunc The scoring function for AI.
	 * 
	 * @return boardStateNode manipulated such that all subsequent actions are children of the original boardStateNode input.
	 */
	public HearthTreeNode doMoves(HearthTreeNode boardStateNode, StateFunction<BoardState> scoreFunc) throws HSException {

		if (lethal_) {
			//if it's lethal, we don't have to do anything ever.  Just play the lethal.
			return null;
		}

		if (timedOut_) {
			//Time's up!  no more thinking... 
			return null;
		}
		
		if (System.currentTimeMillis() - startTime_ > maxTime_) {
			timedOut_ = true;
			return null;
		}
		
		if (boardStateNode.numChildren() > 0) {
			//If this node already has children, just call doMoves on each of its children.
			//This situation can happen, for example, atfer a battle cry
			for (HearthTreeNode child : boardStateNode.getChildren()) {
				this.doMoves(child, scoreFunc);
			}
			return boardStateNode;
		}
		
		boolean lethalFound = false;
		if ((!boardStateNode.data_.isAlive_p0()) || (!boardStateNode.data_.isAlive_p1())) {
			//one of the players is dead, no reason to keep playing
			if (!boardStateNode.data_.isAlive_p1()) {
				lethal_ = true;
				lethalFound = true;
			}
		}
		
		if (!lethalFound) {
			//-----------------------------------------------------------------------------------------
			// Use the Hero ability
			//-----------------------------------------------------------------------------------------
			boolean heroAbilityUsable = false;
			if (!boardStateNode.data_.getHero_p0().hasBeenUsed()) {
				//Case0: Decided to use the hero ability -- Use it on everthing!
				for(int i = 0; i <= boardStateNode.data_.getNumMinions_p0(); ++i) {
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
					newState = newState.data_.getHero_p0().useHeroAbility(0, 0, i, newState, deck_);
					if (newState != null) {
						if (newState instanceof StopNode) {
							
						} else {
							newState = this.doMoves(newState, scoreFunc);
							if (newState != null) boardStateNode.addChild(newState);
							heroAbilityUsable = true;
						}
					}
				}
				for(int i = 0; i <= boardStateNode.data_.getNumMinions_p1(); ++i) {
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
					newState = newState.data_.getHero_p0().useHeroAbility(0, 1, i, newState, deck_);
					if (newState != null) {
						if (newState instanceof StopNode) {
							
						} else {
							newState = this.doMoves(newState, scoreFunc);
							if (newState != null) boardStateNode.addChild(newState);
							heroAbilityUsable = true;
						}
					}
				}
				if (heroAbilityUsable) {
					//Case1: Decided not to use the hero ability
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
					newState.data_.getHero_p0().hasBeenUsed(true);
					newState = this.doMoves(newState, scoreFunc);
					if (newState != null) boardStateNode.addChild(newState);
				}
			}
			
			//-----------------------------------------------------------------------------------------
			// Use the cards in the hand
			//-----------------------------------------------------------------------------------------
			
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
				newState = this.doMoves(newState, scoreFunc);
				if (newState != null) boardStateNode.addChild(newState);
			}
			
			
			int mana = boardStateNode.data_.getMana_p0();
			for (int ic = 0; ic < boardStateNode.data_.getNumCards_hand(); ++ic) {
				if (boardStateNode.data_.getCard_hand_p0(ic).getMana() <= mana && !boardStateNode.data_.getCard_hand_p0(ic).hasBeenUsed()) {
					//we can use this card!  Let's try using it on everything
					for(int i = 0; i <= boardStateNode.data_.getNumMinions_p0() + 1; ++i) {
						HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
						Card card = newState.data_.getCard_hand_p0(ic);
						newState = card.useOn(ic, 0, i, newState, deck_);
						if (newState != null) {
							if (newState instanceof StopNode) {
							} else {
								newState = this.doMoves(newState, scoreFunc);
								if (newState != null) boardStateNode.addChild(newState);
							}
						}
					}
					for(int i = 0; i <= boardStateNode.data_.getNumMinions_p1() + 1; ++i) {
						HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
						Card card = newState.data_.getCard_hand_p0(ic);
						newState = card.useOn(ic, 1, i, newState, deck_);
						if (newState != null) {
							if (newState instanceof StopNode) {
							
							} else {
								newState = this.doMoves(newState, scoreFunc);
								if (newState != null) boardStateNode.addChild(newState);
							}
						}
					}
				}
			}
			
			//-----------------------------------------------------------------------------------------
			// Attack with the minions on the board
			//-----------------------------------------------------------------------------------------
	
			//Use the minions that we have out on the board
			//the case where I choose to not use any more minions
			boolean allAttacked = true;
			for ( final Minion minion : boardStateNode.data_.getMinions_p0()) {
				allAttacked = allAttacked && minion.hasAttacked();
			}
			
			if (!allAttacked) {
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
				for (Minion minion : newState.data_.getMinions_p0()) {
					minion.hasAttacked(true);
				}
				newState = this.doMoves(newState, scoreFunc);
				if (newState != null) boardStateNode.addChild(newState);
			}
			for (int ic = 1; ic < boardStateNode.data_.getNumMinions_p0() + 1; ++ic) {
				final Minion minion = boardStateNode.data_.getMinion_p0(ic-1);
				if (minion.hasAttacked()) {
					continue;
				}
				ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions_p1();
				for(final Integer integer : attackable) {
					int i = integer.intValue();
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
					Minion tempMinion = newState.data_.getMinion_p0(ic-1);
					newState = tempMinion.attack(ic, 1, i, newState, deck_);
					if (newState != null) {
						if (newState instanceof StopNode) {
							
						} else {
							newState = this.doMoves(newState, scoreFunc);
							if (newState != null) boardStateNode.addChild(newState);
						}
					}
				}
			}
		}
		
			
		if (boardStateNode.isLeaf()) {
			//If at this point the node has no children, it is a leaf node.  Compute its board score and store it.
			boardStateNode.setScore(scoreFunc.apply(boardStateNode.data_));
			boardStateNode.setNumNodesTries(1);
		} else {
			//If it is not a leaf, set the score as the maximum score of its children.
			//We can also throw out any children that don't have the highest score (boy, this sounds so wrong...)
			double tmpScore = -1.e300;
			int tmpNumNodesTried = 0;
			Iterator<HearthTreeNode> iter = boardStateNode.getChildren().iterator();
			HearthTreeNode bestBranch = null;
			while (iter.hasNext()) {
				HearthTreeNode child = iter.next();
				tmpNumNodesTried += child.getNumNodesTried();
				if (child.getScore() > tmpScore) {
					tmpScore = child.getScore();
					bestBranch = child;
				}
			}
			boardStateNode.clearChildren();
			boardStateNode.addChild(bestBranch);
			boardStateNode.setScore(tmpScore);
			boardStateNode.setNumNodesTries(tmpNumNodesTried);
		}
		
		return boardStateNode;
	}
	
}
