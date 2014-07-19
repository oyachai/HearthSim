package com.hearthsim.util.tree;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.BoardState;

/**
 * A tree node that stops AI from picking the "best" outcome from its branches. 
 *
 */
public abstract class StopNode extends HearthTreeNode {
	
	public final Card cardUsed_;
	public final Minion minionAttackedWith_;
	public final int usedCardPlayerIndex_;
	public final int usedCardIndex_;
	public final int targetPlayerIndex_;
	public final int targetMinionIndex_;
	
	
	public StopNode(BoardState data, double score, Card cardUsed, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(data, score);
		cardUsed_ = cardUsed;
		minionAttackedWith_ = null;
		usedCardPlayerIndex_ = usedCardPlayerIndex;
		usedCardIndex_ = usedCardIndex;
		targetPlayerIndex_ = targetPlayerIndex;
		targetMinionIndex_ = targetMinionIndex;
	}
	
	public StopNode(BoardState data, double score, Minion minionAttackedWith, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(data, score);
		cardUsed_ = null;
		minionAttackedWith_ = minionAttackedWith;
		usedCardPlayerIndex_ = usedCardPlayerIndex;
		usedCardIndex_ = usedCardIndex;
		targetPlayerIndex_ = targetPlayerIndex;
		targetMinionIndex_ = targetMinionIndex;
	}

	@Override
	public abstract double getScore();

	@Override
	public void setScore(double value) {
	}

	
}
