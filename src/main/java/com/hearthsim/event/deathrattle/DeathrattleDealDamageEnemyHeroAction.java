package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDealDamageEnemyHeroAction extends DeathrattleAction {
	private final byte damage_;
    
	public DeathrattleDealDamageEnemyHeroAction(byte damage) {
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
			toRet = toRet.data_.getHero((thisPlayerIndex + 1) % 2).takeDamage(damage_, thisPlayerIndex, thisPlayerIndex, toRet, deckPlayer0, deckPlayer1, false, false);
		}
		return toRet;
	}
}
