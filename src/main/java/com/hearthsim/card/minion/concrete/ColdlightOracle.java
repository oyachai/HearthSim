package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightOracle extends Minion implements MinionUntargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	
	public ColdlightOracle() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.MURLOC;
	}
	
	/**
	 * Battlecry: All minions lose Divine Shield.  Gain +3/+3 for each Shield lost
	 */
	@Override
	public HearthTreeNode useUntargetableBattlecry_core(
			Minion minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		if (toRet instanceof CardDrawNode)
			((CardDrawNode) toRet).addNumCardsToDraw(2);
		else
			toRet = new CardDrawNode(toRet, 2); //draw two cards
		
		toRet.data_.drawCardFromWaitingPlayerDeck(2);
		return toRet;
	}

}
