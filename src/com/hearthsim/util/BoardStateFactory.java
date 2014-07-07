package com.hearthsim.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;
import com.hearthsim.util.Pair;

public class BoardStateFactory {

	HashMap<BoardState, Integer> boardMap;
	final Deck deck_;
	
	boolean lethal_;
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
		boardMap = new HashMap<BoardState, Integer>(1000000);
		lethal_ = false;
		startTime_ = System.currentTimeMillis();
		curScore_ = -1.e200;
		maxTime_ = maxThinkTime;
	}

	/**
	 * Recursively generate all possible moves
	 * 
	 * This function recursively generates all possible moves that can be done starting from a given BoardState.
	 * The results are stored in a tree structure and returned as a tree of BoardState class.
	 * 
	 * @param boardStateNode The initial BoardState wrapped in a HearthTreeNode.
	 * @param deck The deck that the player is playing with.
	 * 
	 * @return boardStateNode manipulated such that all subsequent actions are children of the original boardStateNode input.
	 */
	public HearthTreeNode<BoardState> doMoves(HearthTreeNode<BoardState> boardStateNode) {

		if (System.currentTimeMillis() - startTime_ > maxTime_) {
			return null;
		}
		
		if (lethal_) {
			//if it's lethal, we don't have to do anything ever.  Just play the lethal.
			return null;
		}
		
		if (boardStateNode.numChildren() > 0) {
			//If this node already has children, just call doMoves on each of its children.
			//This situation can happen, for example, atfer a battle cry
			for (HearthTreeNode<BoardState> child : boardStateNode.getChildren()) {
				this.doMoves(child);
			}
			return boardStateNode;
		}
		
		if (boardMap.containsKey(boardStateNode.data_)) {
			//no need to continue down this path
			return null;
		} else {
			//add it to the list of known states
			boardMap.put(boardStateNode.data_, 1);
		}
		
		if ((!boardStateNode.data_.isAlive_p0()) || (!boardStateNode.data_.isAlive_p1())) {
			//one of the players is dead, no reason to keep playing
			if (!boardStateNode.data_.isAlive_p1())
				lethal_ = true;
			return null;
		}
		
		//-----------------------------------------------------------------------------------------
		// Use the cards in the hand
		//-----------------------------------------------------------------------------------------
		
		//check to see if all the cards have been used already
		boolean allUsed = true;
		for (final Card card : boardStateNode.data_.getCards_hand()) {
			allUsed = allUsed && card.hasBeenUsed();
		}
		
		//the case where I chose not to use any more cards
		if (!allUsed) {
			BoardState newState = (BoardState)boardStateNode.data_.deepCopy();
			for (Card card : newState.getCards_hand()) {
				card.hasBeenUsed(true);
			}
			HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
			newNode = this.doMoves(newNode);
		}
		
		
		int mana = boardStateNode.data_.getMana_p0();
		for (int ic = 0; ic < boardStateNode.data_.getNumCards_hand(); ++ic) {
			if (boardStateNode.data_.getCard_hand(ic).getMana() <= mana && !boardStateNode.data_.getCard_hand(ic).hasBeenUsed()) {
				//we can use this card!  Let's try using it on everything
				for(int i = 0; i <= boardStateNode.data_.getNumMinions_p0() + 1; ++i) {
					HearthTreeNode<BoardState> newState = new HearthTreeNode<BoardState>((BoardState)boardStateNode.data_.deepCopy());
					Card card = newState.data_.getCard_hand(ic);
					newState = card.useOn(ic, 0, i, newState, deck_);
					if (newState != null) {
						HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
						newNode = this.doMoves(newNode);
					}
				}
				for(int i = 0; i <= boardStateNode.data_.getNumMinions_p1() + 1; ++i) {
					HearthTreeNode<BoardState> newState = new HearthTreeNode<BoardState>((BoardState)boardStateNode.data_.deepCopy());
					Card card = newState.data_.getCard_hand(ic);
					newState = card.useOn(ic, 1, i, newState, deck_);
					if (newState != null) {
						HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
						newNode = this.doMoves(newNode);
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
			BoardState newState = (BoardState)boardStateNode.data_.deepCopy();
			for (Minion minion : newState.getMinions_p0()) {
				minion.hasAttacked(true);
			}
			HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
			newNode = this.doMoves(newNode);
		}
		for (int ic = 0; ic < boardStateNode.data_.getNumMinions_p0(); ++ic) {
			final Minion minion = boardStateNode.data_.getMinion_p0(ic);
			if (minion.hasAttacked()) {
				continue;
			}
			ArrayList<Integer> attackable = boardStateNode.data_.getAttackableMinions_p1();
			for(final Integer integer : attackable) {
				int i = integer.intValue();
				HearthTreeNode<BoardState> tempBoard = new HearthTreeNode<BoardState>((BoardState)boardStateNode.data_.deepCopy());
				Minion tempMinion = tempBoard.data_.getMinion_p0(ic);
				HearthTreeNode<BoardState> newState = tempMinion.attack(ic, 1, i, tempBoard, deck_);
				if (newState != null) {
					HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
					newNode = this.doMoves(newNode);
				}
			}
		}
		
		
		return boardStateNode;
	}
	
}
