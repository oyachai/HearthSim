package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;

public class Rogue extends Hero {
	
	public Rogue() {
		this("Rogue", (byte)30);
	}

	public Rogue(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Rogue(
			String name,
			byte attack,
			byte extraAttackUntilTurnEnd,
			byte health,
			byte armor,
			byte weaponCharge,
			boolean windFury,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			boolean frozen,
			boolean hasBeenUsed) {
	
		super(name, attack, extraAttackUntilTurnEnd, health, armor, weaponCharge, windFury, hasAttacked, hasWindFuryAttacked, frozen, hasBeenUsed);
	}
	
	@Override
	public DeepCopyable deepCopy() {
		return new Rogue(
				this.name_, 
				this.attack_,
				this.extraAttackUntilTurnEnd_,
				this.health_,
				this.armor_,
				this.weaponCharge_,
				this.windFury_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.hasBeenUsed
				);
	}
	
	/**
	 * Use the hero ability on a given target
	 * 
	 * Rogue: equip a 1 attack, 2 charge weapon
	 * 
	 *
     * @param targetPlayerModel
     * @param boardState
     * @return
	 */
	@Override
	public HearthTreeNode useHeroAbility_core(
			PlayerModel targetPlayerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = boardState;
		if (targetMinion instanceof Hero && targetPlayerModel == PlayerSide.CURRENT_PLAYER) {
			this.hasBeenUsed = true;
			toRet.data_.setMana_p0(toRet.data_.getMana_p0() - HERO_ABILITY_COST);
			Hero target = (Hero)targetMinion;
			target.setWeaponCharge((byte)2);
			target.setAttack((byte)1);
			return toRet;
		} else {
			return null;
		}
	}

}
