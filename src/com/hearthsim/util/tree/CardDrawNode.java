package com.hearthsim.util.tree;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.util.BoardState;

/**
 * A card draw triggers a stop
 *
 */
public class CardDrawNode extends StopNode {
	
	public CardDrawNode(BoardState data, double score, Card cardUsed, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(data, score, cardUsed, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}
	
	public CardDrawNode(BoardState data, double score, Minion minionAttackedWith, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(data, score, minionAttackedWith, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}

	@Override
	public double getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

}
