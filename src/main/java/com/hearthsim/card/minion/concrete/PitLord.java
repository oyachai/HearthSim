package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class PitLord extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	
	private static final byte SPELL_DAMAGE = 0;
	
	public PitLord() {
	    super();
	    spellDamage_ = SPELL_DAMAGE;
	    heroTargetable_ = HERO_TARGETABLE;
	}

	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_TARGET);
	}
	
	/**
	 * Battlecry: Deal 5 damage to your hero
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
		toRet = PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getHero().takeDamage((byte)5, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, deckPlayer0, deckPlayer1, false, true);
		return toRet;
	}

}
