package com.hearthsim.util.tree;

import com.hearthsim.card.Deck;

/**
 * A tree node that stops AI from picking the "best" outcome from its branches.
 *
 */
public abstract class StopNode extends HearthTreeNode {

    public StopNode(HearthTreeNode origNode) {
        super(origNode.data_, origNode.action, origNode.score_, origNode.depth_);
        children_ = origNode.children_;
    }

    @Deprecated
    public HearthTreeNode finishAllEffects(Deck deckPlayer0, Deck deckPlayer1) {
        return this.finishAllEffects();
    }

    public abstract HearthTreeNode finishAllEffects();
}
