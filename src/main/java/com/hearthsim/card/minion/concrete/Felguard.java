package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class Felguard extends Minion implements MinionUntargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Felguard() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.DEMON;
	}

	/**
	 * Taunt.  Battlecry: Destroy one of your Mana Crystals
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
		toRet.data_.getCurrentPlayer().subtractMaxMana((byte)1);
		return toRet;
	}
	
}
