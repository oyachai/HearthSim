package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleSummonMinionAction extends DeathrattleAction {

	private final int numMinions_;
    private final Class<?> minionClass_;
    
	public DeathrattleSummonMinionAction(Class<?> minionClass, int numMnions) {
		numMinions_ = numMnions;
		minionClass_ = minionClass;
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
		for (int index = 0; index < numMinions_; ++index) {
            try {
            	Minion newMinion = (Minion)minionClass_.newInstance();
            	Minion placementTarget = toRet.data_.getCharacter(playerModel, toRet.data_.getMinions(playerModel).indexOf(minion)); //this minion can't be a hero
            	toRet.data_.removeMinion(minion);
				toRet = newMinion.summonMinion(playerModel, placementTarget, toRet, deckPlayer0, deckPlayer1, false);
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				throw new HSException();
			}
		}
		return toRet;
	}
}
