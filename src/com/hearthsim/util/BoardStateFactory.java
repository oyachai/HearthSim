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
	boolean lethal_;
	static final long maxTime_ = 20000;
	
	long startTime_;
	long curTime_;
	
	double curScore_;
	
	public BoardStateFactory() {
		boardMap = new HashMap<BoardState, Integer>(1000000);
		lethal_ = false;
		startTime_ = System.currentTimeMillis();
		curScore_ = -1.e200;
	}
	

	public HearthTreeNode<BoardState> doMoves(HearthTreeNode<BoardState> boardStateNode, Deck deck) {

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
				this.doMoves(child, deck);
			}
			return boardStateNode;
		}
		
		if (boardMap.containsKey(boardStateNode.data())) {
			//no need to continue down this path
			return null;
		} else {
			//add it to the list of known states
			boardMap.put(boardStateNode.data(), 1);
		}
		
		if ((!boardStateNode.data().isAlive_p0()) || (!boardStateNode.data().isAlive_p1())) {
			//one of the players is dead, no reason to keep playing
			if (!boardStateNode.data().isAlive_p1())
				lethal_ = true;
			return null;
		}
		
		//check to see if all the cards have been used already
		boolean allUsed = true;
		for (final Card card : boardStateNode.data().getCards_hand()) {
			allUsed = allUsed && card.hasBeenUsed();
		}
		
		//the case where I chose not to use any more cards
		if (!allUsed) {
			BoardState newState = (BoardState)boardStateNode.data().deepCopy();
			for (Card card : newState.getCards_hand()) {
				card.hasBeenUsed(true);
			}
			HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
			newNode = this.doMoves(newNode, deck);
		}
		
		
		int mana = boardStateNode.data().getMana_p0();
		for (int ic = 0; ic < boardStateNode.data().getNumCards_hand(); ++ic) {
			if (boardStateNode.data().getCard_hand(ic).getMana() <= mana && !boardStateNode.data().getCard_hand(ic).hasBeenUsed()) {
				//we can use this card!  Let's try using it on everything
				for(int i = 0; i <= boardStateNode.data().getNumMinions_p0() + 1; ++i) {
					BoardState newState = (BoardState)boardStateNode.data().deepCopy();
					Card card = newState.getCard_hand(ic);
					newState = card.useOn(ic, 0, i, newState);
					if (newState != null) {
						HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
						newNode = this.doMoves(newNode, deck);
					}
				}
				for(int i = 0; i <= boardStateNode.data().getNumMinions_p1() + 1; ++i) {
					BoardState newState = (BoardState)boardStateNode.data().deepCopy();
					Card card = newState.getCard_hand(ic);
					newState = card.useOn(ic, 1, i, newState);
					if (newState != null) {
						HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
						newNode = this.doMoves(newNode, deck);
					}
				}
			}
		}
		
		//Use the minions that we have out on the board
		//the case where I choose to not use any more minions
		boolean allAttacked = true;
		for ( final Minion minion : boardStateNode.data().getMinions_p0()) {
			allAttacked = allAttacked && minion.hasAttacked();
		}
		
		if (!allAttacked) {
			BoardState newState = (BoardState)boardStateNode.data().deepCopy();
			for (Minion minion : newState.getMinions_p0()) {
				minion.hasAttacked(true);
			}
			HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
			newNode = this.doMoves(newNode, deck);
		}
		for (int ic = 0; ic < boardStateNode.data().getNumMinions_p0(); ++ic) {
			final Minion minion = boardStateNode.data().getMinion_p0(ic);
			if (minion.hasAttacked()) {
				continue;
			}
			ArrayList<Integer> attackable = boardStateNode.data().getAttackableMinions_p1();
			for(final Integer integer : attackable) {
				int i = integer.intValue();
				BoardState tempBoard = (BoardState)boardStateNode.data().deepCopy();
				Minion tempMinion = tempBoard.getMinion_p0(ic);
				BoardState newState = tempMinion.useOn(ic, 1, i, tempBoard);
				if (newState != null) {
					HearthTreeNode<BoardState> newNode = boardStateNode.addChild(newState);
					newNode = this.doMoves(newNode, deck);
				}
			}
		}
		
		
		return boardStateNode;
	}
	
}
