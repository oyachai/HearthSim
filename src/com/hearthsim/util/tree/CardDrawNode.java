package com.hearthsim.util.tree;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;

/**
 * A card draw triggers a stop
 *
 */
public class CardDrawNode extends StopNode {
	
	public CardDrawNode(HearthTreeNode origNode, Card cardUsed, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(origNode, cardUsed, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}
	
	public CardDrawNode(HearthTreeNode origNode, Minion minionAttackedWith, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(origNode, minionAttackedWith, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}

	@Override
	public double getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

}
