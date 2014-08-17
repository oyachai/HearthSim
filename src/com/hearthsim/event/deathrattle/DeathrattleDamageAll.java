package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDamageAll extends DeathrattleAction {
	
	private final byte damage_;
    
	public DeathrattleDamageAll(byte damage) {
		damage_ = damage;
	}
	
	@Override
	public HearthTreeNode performAction(
			Minion minion,
			int thisPlayerIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1) 
		throws HSException
	{
		HearthTreeNode toRet = super.performAction(minion, thisPlayerIndex, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			toRet = toRet.data_.getHero_p0().takeDamage(damage_, thisPlayerIndex, 0, boardState, deckPlayer0, deckPlayer1, false, false);
			for(Minion aMinion : toRet.data_.getMinions_p0()) {
				toRet = aMinion.takeDamage(damage_, thisPlayerIndex, 0, toRet, deckPlayer0, deckPlayer1, false, false);
			}
			toRet = toRet.data_.getHero_p1().takeDamage(damage_, thisPlayerIndex, 1, boardState, deckPlayer0, deckPlayer1, false, false);
			for(Minion aMinion : toRet.data_.getMinions_p1()) {
				toRet = aMinion.takeDamage(damage_, thisPlayerIndex, 1, toRet, deckPlayer0, deckPlayer1, false, false);
			}
		}
		return toRet;
	}

}
