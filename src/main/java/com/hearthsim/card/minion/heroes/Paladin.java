package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.SilverHandRecruit;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.tree.HearthTreeNode;

public class Paladin extends Hero {

	public Paladin() {
		this("Paladin", (byte)30);
	}

	public Paladin(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Paladin(
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
		return new Paladin(
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
	 * Paladin: summon a 1/1 Silver Hand Recruit
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
		if (currentPlayerBoardFull(boardState))
			return null;

		HearthTreeNode toRet = boardState;

		if (isHero(targetMinion) && targetPlayerSide == PlayerSide.CURRENT_PLAYER) {
			this.hasBeenUsed = true;
			toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
			Minion theRecruit = new SilverHandRecruit();
			BaseEntity targetLocation = toRet.data_.getCharacter(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getNumMinions());
			toRet = theRecruit.summonMinion(targetPlayerSide, targetLocation, toRet, deckPlayer0, deckPlayer1, false);
		} else {
			return null;
		}
	
		return toRet;
	}
}
