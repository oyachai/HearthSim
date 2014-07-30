package com.hearthsim.util.tree;

import com.hearthsim.card.Deck;

public class SecretNode extends StopNode {

	public SecretNode(HearthTreeNode origNode) {
		super(origNode);
	}
	
	@Override
	public HearthTreeNode finishAllEffects(Deck deck) {
		// TODO Auto-generated method stub
		return null;
	}

}
