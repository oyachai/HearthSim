package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDealDamageEnemyHeroAction extends DeathrattleAction {
	private final byte damage_;
    
	public DeathrattleDealDamageEnemyHeroAction(byte damage) {
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
            PlayerSide otherPlayer = playerSide.getOtherPlayer();
			toRet = otherPlayer.getPlayer(toRet).getHero().takeDamage(damage_, playerSide, playerSide, toRet, deckPlayer0, deckPlayer1, false, false);
		}
		return toRet;
	}
}
