package com.hearthsim.util.tree;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.BoardState;

public class SecretNode extends StopNode {

	public SecretNode(BoardState data, double score, Card cardUsed, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(data, score, cardUsed, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}
	
	public SecretNode(BoardState data, double score, Minion minionAttackedWith, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(data, score, minionAttackedWith, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}
	@Override
	public double getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

}
