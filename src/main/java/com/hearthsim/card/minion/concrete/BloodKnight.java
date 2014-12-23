package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BloodKnight extends Minion implements MinionUntargetableBattlecry {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public BloodKnight() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

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
		for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
			if (minion != this && minion.getDivineShield()) {
				minion.setDivineShield(false);
				this.setHealth((byte)(this.getHealth() + 3));
				this.setAttack((byte)(this.getAttack() + 3));
			}
		}
		for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
			if (minion.getDivineShield()) {
				minion.setDivineShield(false);
				this.setHealth((byte)(this.getHealth() + 3));
				this.setAttack((byte)(this.getAttack() + 3));
			}
		}
		return toRet;
	}
	
}
