package com.hearthsim.card.spellcard;

import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.Collection;

public interface SpellRandomInterface {
    public Collection<HearthTreeNode> createChildren(PlayerSide originSide, int originIndex, HearthTreeNode boardState);
}
