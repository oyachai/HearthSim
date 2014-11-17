package com.hearthsim.util.factory;

import java.util.ArrayList;

import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public interface ChildNodeCreator {

	public ArrayList<HearthTreeNode> createAttackChildren(HearthTreeNode boardStateNode) throws HSException;
	public ArrayList<HearthTreeNode> createPlayCardChildren(HearthTreeNode boardStateNode) throws HSException;
	public ArrayList<HearthTreeNode> createHeroAbilityChildren(HearthTreeNode boardStateNode) throws HSException;

}
