package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDamageAll extends DeathrattleAction {
	
	private final byte damage_;
    
	public DeathrattleDamageAll(byte damage) {
		damage_ = damage;
	}
	
	@Override
	public HearthTreeNode performAction(
			Minion minion,
			PlayerModel playerModel,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1) 
		throws HSException
	{
		HearthTreeNode toRet = super.performAction(minion, playerModel, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			toRet = toRet.data_.getCurrentPlayerHero().takeDamage(damage_, playerModel, toRet.data_.getCurrentPlayer(), boardState, deckPlayer0, deckPlayer1, false, false);
			for(Minion aMinion : toRet.data_.getCurrentPlayer().getMinions()) {
				toRet = aMinion.takeDamage(damage_, playerModel, toRet.data_.getCurrentPlayer(), toRet, deckPlayer0, deckPlayer1, false, false);
			}
			toRet = toRet.data_.getWaitingPlayerHero().takeDamage(damage_, playerModel, toRet.data_.getWaitingPlayer(), boardState, deckPlayer0, deckPlayer1, false, false);
			for(Minion aMinion : toRet.data_.getWaitingPlayer().getMinions()) {
				toRet = aMinion.takeDamage(damage_, playerModel, toRet.data_.getWaitingPlayer(), toRet, deckPlayer0, deckPlayer1, false, false);
			}
		}
		return toRet;
	}

}
