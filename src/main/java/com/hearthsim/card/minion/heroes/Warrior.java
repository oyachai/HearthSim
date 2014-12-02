package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;

public class Warrior extends Hero {
	
	public Warrior() {
		this("Warrior", (byte)30);
	}

	public Warrior(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Warrior(
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
	
	
	public DeepCopyable deepCopy() {
		return new Warrior(
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
	 * Warrior: +2 armor
	 * 
	 *
     *
     * @param targetPlayerSide
     * @param boardState
     * @return
	 */
	
	public HearthTreeNode useHeroAbility_core(
			PlayerSide targetPlayerSide,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		HearthTreeNode toRet = boardState;
		if (isHero(targetMinion) && targetPlayerSide == PlayerSide.CURRENT_PLAYER) {
			this.hasBeenUsed = true;
			toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
			((Hero)targetMinion).setArmor((byte)(((Hero)targetMinion).getArmor() + 2));
			return toRet;
		} else {
			return null;
		}
	}

}
