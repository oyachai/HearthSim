package com.hearthsim.util;

import java.util.ArrayList;
import java.util.Iterator;

import sun.awt.util.IdentityLinkedList;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.BoardState.MinionPlayerIDPair;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.StopNode;

public class BoardStateFactory {

	final Deck deckPlayer0_;
	final Deck deckPlayer1_;
	
	boolean lethal_;
	boolean timedOut_;
	public final long maxTime_;
	
	long startTime_;
	long curTime_;
	
	double curScore_;
	
	/**
	 * Constructor
	 * 
	 * maxThinkTime defaults to 10000 milliseconds (10 seconds)
	 */
	public BoardStateFactory(Deck deckPlayer0, Deck deckPlayer1) {
		this(deckPlayer0, deckPlayer1, 10000);
	}
	
        /**
         * Constructor
         * 
         * @param deckPlayer0
         * @param deckPlayer1
	 * @param maxThinkTime The maximum amount of time in milliseconds the factory is allowed to spend on generating the simulation tree.
	 */
	public BoardStateFactory(Deck deckPlayer0, Deck deckPlayer1, long maxThinkTime) {
		deckPlayer0_ = deckPlayer1;
		deckPlayer1_ = deckPlayer1;
		lethal_ = false;
		startTime_ = System.currentTimeMillis();
		curScore_ = -1.e200;
		maxTime_ = maxThinkTime;
		timedOut_ = false;
	}
	
	public boolean didTimeOut() {
		return timedOut_;
	}
	
	public void resetTimeOut() {
		startTime_ = System.currentTimeMillis();
		timedOut_ = false;
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
	public HearthTreeNode doMoves(HearthTreeNode boardStateNode, ArtificialPlayer ai) throws HSException {

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
			//This situation can happen, for example, after a battle cry
			for (HearthTreeNode child : boardStateNode.getChildren()) {
				this.doMoves(child, ai);
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
				//Case0: Decided to use the hero ability -- Use it on everything!
				for(int i = 0; i <= boardStateNode.data_.getNumMinions_p0(); ++i) {
					Minion targetMinion = boardStateNode.data_.getCharacter_p0(i);
					if (boardStateNode.data_.getHero_p0().canBeUsedOn(0, targetMinion)) {
						HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
						Minion copiedTargetMinion = newState.data_.getCharacter_p0(i);
						newState = newState.data_.getHero_p0().useHeroAbility(0, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_);
						if (newState != null) {
							newState = this.doMoves(newState, ai);
							if (newState != null) boardStateNode.addChild(newState);
							heroAbilityUsable = true;
						}
					}
				}
				for(int i = 0; i <= boardStateNode.data_.getNumMinions_p1(); ++i) {
					Minion targetMinion = boardStateNode.data_.getCharacter_p1(i);
					if (boardStateNode.data_.getHero_p0().canBeUsedOn(1, targetMinion)) {
						HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
						Minion copiedTargetMinion = newState.data_.getCharacter_p1(i);
						newState = newState.data_.getHero_p0().useHeroAbility(1, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_);
						if (newState != null) {
							newState = this.doMoves(newState, ai);
							if (newState != null) boardStateNode.addChild(newState);
							heroAbilityUsable = true;
						}
					}
				}
				if (heroAbilityUsable) {
					//Case1: Decided not to use the hero ability
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
					newState.data_.getHero_p0().hasBeenUsed(true);
					newState = this.doMoves(newState, ai);
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
				newState = this.doMoves(newState, ai);
				if (newState != null) boardStateNode.addChild(newState);
			}
			
			
			int mana = boardStateNode.data_.getMana_p0();
			for (int ic = 0; ic < boardStateNode.data_.getNumCards_hand(); ++ic) {
				if (boardStateNode.data_.getCard_hand_p0(ic).getMana() <= mana && !boardStateNode.data_.getCard_hand_p0(ic).hasBeenUsed()) {
					//we can use this card!  Let's try using it on everything
					for(int i = 0; i <= boardStateNode.data_.getNumMinions_p0(); ++i) {
						Minion targetMinion = boardStateNode.data_.getCharacter_p0(i);
						if (boardStateNode.data_.getCard_hand_p0(ic).canBeUsedOn(0, targetMinion)) {
							HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
							Minion copiedTargetMinion = newState.data_.getCharacter_p0(i);
							Card card = newState.data_.getCard_hand_p0(ic);
							newState = card.useOn(0, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_);
							if (newState != null) {
								newState = this.doMoves(newState, ai);
								if (newState != null) boardStateNode.addChild(newState);
							}
						}
					}
					for(int i = 0; i <= boardStateNode.data_.getNumMinions_p1(); ++i) {
						Minion targetMinion = boardStateNode.data_.getCharacter_p1(i);
						if (boardStateNode.data_.getCard_hand_p0(ic).canBeUsedOn(1, targetMinion)) {
							HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
							Minion copiedTargetMinion = newState.data_.getCharacter_p1(i);
							Card card = newState.data_.getCard_hand_p0(ic);
							newState = card.useOn(1, copiedTargetMinion, newState, deckPlayer0_, deckPlayer1_);
							if (newState != null) {
								newState = this.doMoves(newState, ai);
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
			boolean allAttacked = boardStateNode.data_.getHero_p0().hasAttacked();
			for ( final Minion minion : boardStateNode.data_.getMinions_p0()) {
				allAttacked = allAttacked && minion.hasAttacked();
			}
			
			if (!allAttacked && boardStateNode.data_.getNumMinions_p0() > 0) {
				HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
				for (Minion minion : newState.data_.getMinions_p0()) {
					minion.hasAttacked(true);
				}
				newState.data_.getHero_p0().hasAttacked(true);
				newState = this.doMoves(newState, ai);
				if (newState != null) boardStateNode.addChild(newState);
			}
			//attack with hero if possible
			if (!boardStateNode.data_.getHero_p0().hasAttacked() && (boardStateNode.data_.getHero_p0().getAttack() + boardStateNode.data_.getHero_p0().getExtraAttackUntilTurnEnd()) > 0) {
				ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions_p1();
				for(final Integer integer : attackable) {
					int i = integer.intValue();
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
					Minion targetMinion = newState.data_.getCharacter_p1(i);
					newState = newState.data_.getHero_p0().attack(1, targetMinion, newState, deckPlayer0_, deckPlayer1_);
					if (newState != null) {
						newState = this.doMoves(newState, ai);
						if (newState != null) boardStateNode.addChild(newState);
					}
				}				
			}
			//attack with minion
			for (int ic = 0; ic < boardStateNode.data_.getNumMinions_p0(); ++ic) {
				final Minion minion = boardStateNode.data_.getMinion_p0(ic);
				if (minion.hasAttacked()) {
					continue;
				}
				ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions_p1();
				for(final Integer integer : attackable) {
					int i = integer.intValue();
					HearthTreeNode newState = new HearthTreeNode((BoardState)boardStateNode.data_.deepCopy());
					Minion targetMinion = newState.data_.getCharacter_p1(i);
					Minion tempMinion = newState.data_.getMinion_p0(ic);
					newState = tempMinion.attack(1, targetMinion, newState, deckPlayer0_, deckPlayer1_);
					if (newState != null) {
						newState = this.doMoves(newState, ai);
						if (newState != null) boardStateNode.addChild(newState);
					}
				}
			}
		}
		
			
		if (boardStateNode.isLeaf()) {
			//If at this point the node has no children, it is a leaf node.  Compute its board score and store it.
			boardStateNode.setScore(ai.boardScore(boardStateNode.data_));
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
				double theScore = child.getScore();
				if (child instanceof CardDrawNode)
					theScore += ((CardDrawNode)child).cardDrawScore(deckPlayer0_, ai);
				if (theScore > tmpScore) {
					tmpScore = child.getScore();
					bestBranch = child;
				}
			}

			if (bestBranch instanceof StopNode) {
				bestBranch.clearChildren(); //cannot continue past a StopNode
			}
			boardStateNode.clearChildren();
			boardStateNode.addChild(bestBranch);
			boardStateNode.setScore(tmpScore);
			boardStateNode.setNumNodesTries(tmpNumNodesTried);
		}
		
		return boardStateNode;
	}
	
	
	
	/**
	 * Handles dead minions
	 * 
	 * For each dead minion, the function calls its deathrattle in the correct order, and then removes the dead minions from the board.
	 * 
	 * @return true if there are dead minions left (minions might have died during deathrattle).  false otherwise.
	 * @throws HSException 
	 */
	public static HearthTreeNode handleDeadMinions(HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		HearthTreeNode toRet = boardState;
		IdentityLinkedList<MinionPlayerIDPair> deadMinions = new IdentityLinkedList<MinionPlayerIDPair>();
		for (MinionPlayerIDPair minionIdPair : toRet.data_.getAllMinionsFIFOList()) {
			if (minionIdPair.minion_.getTotalHealth() <= 0) {
				deadMinions.add(minionIdPair);
			}
		}
		for (MinionPlayerIDPair minionIdPair : deadMinions) {
			toRet = minionIdPair.minion_.destroyed(minionIdPair.playerIndex_, toRet, deckPlayer0, deckPlayer1);
			toRet.data_.removeMinion(minionIdPair);
		}
		if (toRet.data_.hasDeadMinions())
			return BoardStateFactory.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		else
			return toRet;
	}
	
	
}


