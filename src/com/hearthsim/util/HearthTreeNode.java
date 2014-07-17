package com.hearthsim.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree that keeps track of possible game states
 *
 */
public class HearthTreeNode<T> {

	public final T data_;
	HearthTreeNode<T> parent_;
	List<HearthTreeNode<T>> children_;
	
	private class NodeValPair {
		public final HearthTreeNode<T> node_;
		public final double value_;
		
		NodeValPair(HearthTreeNode<T> node, double value) {
			node_ = node;
			value_ = value;
		}
	}
	
	public HearthTreeNode(T data) {
		data_ = data;
		parent_ = null;
		children_ = null;
	}
	
	public HearthTreeNode<T> addChild(T childData) {
		HearthTreeNode<T> childNode = new HearthTreeNode<T>(childData);
		childNode.parent_ = this;
		if (children_ == null) {
			children_ = new ArrayList<HearthTreeNode<T>>();
		}
		children_.add(childNode);
		return childNode;
	}
	
	public HearthTreeNode<T> addChild(HearthTreeNode<T> node) {
		node.parent_ = this;
		if (children_ == null) {
			children_ = new ArrayList<HearthTreeNode<T>>();
		}
		children_.add(node);
		return node;
	}
	
	public List<HearthTreeNode<T>> getChildren() {
		return children_;
	}
	
	public int numChildren() {
		if (children_ == null)
			return 0;
		else {
			return children_.size();
		}
	}
	
	public boolean isLeaf() {
		return children_ == null || children_.size() == 0;
	}
	
	/**
	 * Returns the node below this node that result in the highest value when a given function is applied
	 * 
	 * @param func Function to apply to each node
	 * @return
	 */
	public HearthTreeNode<T> findMaxOfFunc(StateFunction<T> func) {
		NodeValPair nvp = this.findMaxOfFuncImpl(func);
		return nvp.node_;
	}
	
	private NodeValPair findMaxOfFuncImpl(StateFunction<T> func) {
		if (this.isLeaf())
			return new NodeValPair(this, func.apply(this.data_));

		NodeValPair maxNode = null;
		double maxSoFar = -1.e300;
		for (final HearthTreeNode<T> child : children_) {
			NodeValPair maxOfChild = child.findMaxOfFuncImpl(func);
			if (maxOfChild.value_ > maxSoFar) {
				maxSoFar = maxOfChild.value_;
				maxNode = maxOfChild;
			}
		}
		return maxNode;
	}
	
	/**
	 * Get the number of leaf nodes below this node
	 * 
	 * @return
	 */
	public int numLeaves() {
		if (children_ == null || children_.size() == 0)
			return 1;
		int toRet = 0;
		for (final HearthTreeNode<T> child : children_) {
			toRet += child.numLeaves();
		}
		return toRet;
	}	
	
	public String toString() {
		String toRet = "{";
		toRet = toRet + "\"data\": " + data_ + ", ";
		toRet = toRet + "\"children\": [";
		if (children_ != null) {
			boolean hasContent = false;
			for (final HearthTreeNode<T> child : children_) {
				toRet = toRet + child + ", ";
				hasContent = true;
			}
			if (hasContent) {
				toRet = toRet.substring(0, toRet.length() - 2);				
			}
		}
		toRet = toRet + "]";
		toRet = toRet + "}";
		return toRet;
	}
	
}
