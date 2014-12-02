package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDamageAllMinions extends DeathrattleAction {

	private final byte damage_;
    
	public DeathrattleDamageAllMinions(byte damage) {
		damage_ = damage;
	}
	
	@Override
	public HearthTreeNode performAction(
			BaseEntity minion,
			PlayerSide playerSide,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1) 
		throws HSException
	{
		HearthTreeNode toRet = super.performAction(minion, playerSide, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			for(BaseEntity aMinion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
				toRet = aMinion.takeDamage(damage_, playerSide, PlayerSide.WAITING_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
			}
			for(BaseEntity aMinion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
				toRet = aMinion.takeDamage(damage_, playerSide, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
			}
		}
		return toRet;
	}

}
