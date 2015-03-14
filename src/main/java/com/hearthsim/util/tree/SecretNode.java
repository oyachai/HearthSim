package com.hearthsim.util.tree;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;

public abstract class SecretNode extends StopNode {

    public SecretNode(HearthTreeNode origNode) {
        super(origNode);
    }
}
