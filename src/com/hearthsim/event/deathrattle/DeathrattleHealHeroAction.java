package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleHealHeroAction extends DeathrattleAction {

	private final byte amount_;
	private final boolean targetEnemyHero_;
    
	public DeathrattleHealHeroAction(byte amount, boolean targetEnemyHero) {
		amount_ = amount;
		targetEnemyHero_ = targetEnemyHero;
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
			int targetPlayerIndex = targetEnemyHero_ ? (thisPlayerIndex + 1) % 2 : thisPlayerIndex;
			toRet = toRet.data_.getHero(targetPlayerIndex).takeHeal(amount_, targetPlayerIndex, toRet, deckPlayer0, deckPlayer1);
		}
		return toRet;
	}

}
