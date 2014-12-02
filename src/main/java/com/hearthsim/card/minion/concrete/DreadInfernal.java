package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;import com.hearthsim.entity.BaseEntity;
import com.hearthsim.card.minion.Demon;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class DreadInfernal extends Demon {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public DreadInfernal() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
	
	
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_TARGET);
	}
	
	/**
	 * Battlecry: Deals 1 damage to all characters
	 */
	
	public HearthTreeNode useUntargetableBattlecry_core(
			BaseEntity minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
		HearthTreeNode toRet = boardState;
		toRet = toRet.data_.getCurrentPlayerHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
		for (BaseEntity minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if (minion != this)
				toRet = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
		}
		
		toRet = toRet.data_.getWaitingPlayerHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
		for (BaseEntity minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			toRet = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
		}
		
		return toRet;
	}

}
