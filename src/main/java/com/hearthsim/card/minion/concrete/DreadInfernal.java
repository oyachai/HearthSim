package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DreadInfernal extends Minion implements MinionUntargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public DreadInfernal() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.DEMON;
	}
	
	/**
	 * Battlecry: Deals 1 damage to all characters
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
		toRet = toRet.data_.getCurrentPlayerHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1, false, false);
		for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if (minion != this)
				toRet = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
		}
		
		toRet = toRet.data_.getWaitingPlayerHero().takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
		for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			toRet = minion.takeDamage((byte)1, PlayerSide.CURRENT_PLAYER, PlayerSide.WAITING_PLAYER, boardState, deckPlayer0, deckPlayer1, false, false);
		}
		
		return toRet;
	}

}
