package com.hearthsim.util.tree;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;

public class RandomEffectNode extends StopNode {

	public RandomEffectNode(HearthTreeNode origNode, Card cardUsed, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(origNode, cardUsed, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}
	
	public RandomEffectNode(HearthTreeNode origNode, Minion minionAttackedWith, int usedCardPlayerIndex, int usedCardIndex, int targetPlayerIndex, int targetMinionIndex) {
		super(origNode, minionAttackedWith, usedCardPlayerIndex, usedCardIndex, targetPlayerIndex, targetMinionIndex);
	}

	@Override
	public HearthTreeNode finishAllEffects(Deck deck) {
		// TODO Auto-generated method stub
		return null;
	}

}
