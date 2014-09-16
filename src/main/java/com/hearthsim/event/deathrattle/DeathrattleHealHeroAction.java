package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
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
			PlayerSide playerSide,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1) 
		throws HSException
	{
		HearthTreeNode toRet = super.performAction(minion, playerSide, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
            PlayerModel otherPlayer = boardState.data_.getOtherPlayer(playerSide);
            PlayerModel targetPlayerModel = targetEnemyHero_ ? otherPlayer : playerSide;
			toRet = toRet.data_.getHero(targetPlayerModel).takeHeal(amount_, targetPlayerModel, toRet, deckPlayer0, deckPlayer1);
		}
		return toRet;
	}

}
