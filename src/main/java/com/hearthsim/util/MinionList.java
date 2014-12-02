package com.hearthsim.util;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;

/**
 * A list of minion
 * 
 * This class represents a list of minions on the board.  
 * The class differs from a standard LinkedList; it is
 * backed by 2 lists, one that keeps the positional order
 * of minions and one that keeps the temporal order of the
 * minion.  Whenever simultaneous event happens to multiple
 * minions, we must iterate over the temporal ordered list.
 *
 */
public class MinionList extends IdentityLinkedList<BaseEntity> {

//	IdentityLinkedList<Minion> fifoList_;
//	
//	public MinionList() {
//		super();
//		fifoList_ = new IdentityLinkedList<Minion>();
//	}
//	
//	@Override
//	public void add(int index, BaseEntity minion) {
//		super.add(index, minion);
//		fifoList_.addLast(minion);
//	}
//
//	@Override
//	public boolean add(BaseEntity minion) {
//		fifoList_.addLast(minion);
//		return super.add(minion);
//	}
//
//	@Override
//	public void addLast(BaseEntity minion) {
//		fifoList_.addLast(minion);
//		super.addLast(minion);
//	}
//
//	@Override
//	public void addFirst(BaseEntity minion) {
//		fifoList_.addLast(minion);
//		super.addFirst(minion);
//	}
//
//	@Override
//	public Minion remove(int index) {
//		Minion toRet = super.remove(index);
//		fifoList_.remove(toRet);
//		return toRet;
//	}
//
//	@Override
//	public boolean remove(Object minion) {
//		return super.remove(minion) && fifoList_.remove(minion);
//	}
//	
//	/**
//	 * Returns an iterator in temporal (first placed, first out) order
//	 * 
//	 */
//	@Override
//	public Iterator<Minion> iterator() {
//		return fifoList_.iterator();
//	}
//	
//	public Iterator<Minion> positionIterator() {
//		return super.iterator();
//	}
//	
//	@Override
//	public void clear() {
//		fifoList_.clear();
//		super.clear();
//	}
}
