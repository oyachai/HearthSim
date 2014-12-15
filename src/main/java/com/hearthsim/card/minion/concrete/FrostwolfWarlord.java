package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;


public class FrostwolfWarlord extends Minion implements MinionUntargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public FrostwolfWarlord() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	
	/**
	 * Battlecry: gain +1/+1 for each friendly minion on the battlefield
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
		int numBuffs = PlayerSide.CURRENT_PLAYER.getPlayer(boardState).getNumMinions() - 1; //Don't count the Warlord itself
		this.setAttack((byte)(this.getAttack() + numBuffs));
		this.setHealth((byte)(this.getHealth() + numBuffs));
		this.setMaxHealth((byte)(this.getMaxHealth() + numBuffs));
		return toRet;
	}

}
