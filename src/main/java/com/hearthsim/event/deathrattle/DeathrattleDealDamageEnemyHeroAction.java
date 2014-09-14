package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDealDamageEnemyHeroAction extends DeathrattleAction {
	private final byte damage_;
    
	public DeathrattleDealDamageEnemyHeroAction(byte damage) {
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
            PlayerModel otherPlayer = boardState.data_.getOtherPlayer(playerModel);
			toRet = otherPlayer.getHero().takeDamage(damage_, playerModel, playerModel, toRet, deckPlayer0, deckPlayer1, false, false);
		}
		return toRet;
	}
}
