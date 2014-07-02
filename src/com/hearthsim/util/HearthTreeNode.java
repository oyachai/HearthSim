package com.hearthsim.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree that keeps track of possible game states
 * @author oyachai
 *
 */
public class HearthTreeNode<T> {

	T data_;
	HearthTreeNode<T> parent_;
	List<HearthTreeNode<T>> children_;
	
	
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
	
	public T data() {
		return data_;
	}
	
	public boolean isLeaf() {
		if (children_ == null) {
			return true;
		}
		return children_.size() == 0;
	}
	
	public List<HearthTreeNode<T>> getAllLeaves() {
		List<HearthTreeNode<T>> toRet = new ArrayList<HearthTreeNode<T>>();
		if (children_ == null) {
			toRet.add(this);
			return toRet;
		}
		if (children_.size() == 0) {
			toRet.add(this);
			return toRet;
		}
		for (final HearthTreeNode<T> child : children_) {
			List<HearthTreeNode<T>> childLeaves = child.getAllLeaves();
			for (final HearthTreeNode<T> leaf : childLeaves) {
				toRet.add(leaf);
			}
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
