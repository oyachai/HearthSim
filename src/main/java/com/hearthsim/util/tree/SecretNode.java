package com.hearthsim.util.tree;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;

public class SecretNode extends StopNode {

    public SecretNode(HearthTreeNode origNode) {
        super(origNode);
    }

    @Override
    public HearthTreeNode finishAllEffects(Deck deckPlayer0, Deck deckPlayer1) throws HSException  {
        // TODO Auto-generated method stub
        return null;
    }

}
