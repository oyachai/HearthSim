package com.hearthsim.card.minion;

import org.json.JSONObject;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

public class Hero extends BaseEntity
{
	protected static final byte HERO_ABILITY_COST = 2;  //Assumed to be 2 for all heroes
	
	protected byte weaponCharge_;
	protected byte armor_;
	
	public Hero() {
		this("NoHero", (byte)30);
	}

	public Hero(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Hero(
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
	
		super(name, (byte)0, attack, health, (byte)0, extraAttackUntilTurnEnd, (byte)0, (byte)30, (byte)30, (byte)0, (byte)0, false, false, windFury, false, hasAttacked, hasWindFuryAttacked, frozen, false, false, true, false, false, false, false, null, null, false, hasBeenUsed);
		armor_ = armor;
		weaponCharge_ = weaponCharge;
	}


	public byte getWeaponCharge() {
		return weaponCharge_;
	}
	
	public void setWeaponCharge(byte weaponCharge) {
		weaponCharge_ = weaponCharge;
	}
	
	public byte getArmor() {
		return armor_;
	}
	
	public void setArmor(byte armor) {
		armor_ = armor;
	}
	
	@Override
	public DeepCopyable deepCopy() {
		return new Hero(
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
	 * 
	 * Attack with the hero
	 * 
	 * A hero can only attack if it has a temporary buff, such as weapons
	 * 
	 *
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode attack(
			PlayerSide targetMinionPlayerSide,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		
		if (attack_ + extraAttackUntilTurnEnd_ == 0) {
			return null;
		}

		if (this.weaponCharge_ == 0 && this.extraAttackUntilTurnEnd_ == 0) {
			return null;
		}
		
		//this is somewhat redundant, but it must be done here...
		if (frozen_) {
			this.hasAttacked_ = true;
			this.frozen_ = false;
			return boardState;
		}
		
		
		HearthTreeNode toRet = super.attack(targetMinionPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1);

        if (toRet != null && this.weaponCharge_ > 0) {
            this.weaponCharge_ -= 1;
            if (this.weaponCharge_ == 0) {
                this.attack_ = 0;
            }
        }
        return toRet;
	}
	
	@Override
    public boolean canBeUsedOn(PlayerSide playerSide, BaseEntity minion, BoardModel boardModel) {
		if (hasBeenUsed)
			return false;
		if (!minion.isHeroTargetable())
			return false;
		return true;
    }

	public final HearthTreeNode useHeroAbility(
			PlayerSide targetPlayerSide,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		return this.useHeroAbility(targetPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1, false);
	}
	/**
	 * Use the hero ability on a given target
	 * 
	 *
     *
     * @param targetPlayerSide
     * @param targetMinion The target minion
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @return
	 */
	public final HearthTreeNode useHeroAbility(
			PlayerSide targetPlayerSide,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (boardState.data_.getCurrentPlayer().getMana() < HERO_ABILITY_COST)
			return null;
		
		HearthTreeNode toRet = this.useHeroAbility_core(targetPlayerSide, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);
		}
		return toRet;
	}
	
	public HearthTreeNode useHeroAbility_core(
			PlayerSide targetPlayerSide,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		return null;
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Overridden from Minion.  Need to handle armor.
	 *  @param damage The amount of damage to take
	 * @param attackPlayerSide The player index of the attacker.  This is needed to do things like +spell damage.
     * @param thisPlayerSide
     * @param boardState
     * @param deckPlayer0 The deck of player0
     * @param isSpellDamage
     * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode takeDamage(
			byte damage,
			PlayerSide attackPlayerSide,
			PlayerSide thisPlayerSide,
			HearthTreeNode boardState,
			Deck deckPlayer0, 
			Deck deckPlayer1,
			boolean isSpellDamage,
			boolean handleMinionDeath)
		throws HSException
	{
		HearthTreeNode toRet = boardState;
		byte damageRemaining = (byte)(damage - armor_);
		if (damageRemaining > 0) {
			armor_ = 0;
			toRet = super.takeDamage(damageRemaining, attackPlayerSide, thisPlayerSide, toRet, deckPlayer0, deckPlayer1, isSpellDamage, handleMinionDeath);
		} else {
			armor_ = (byte)(armor_ - damage);
		}
		return toRet;
	}
	
	
	/**
	 * End the turn and resets the card state
	 * 
	 * This function is called at the end of the turn.  Any derived class must override it and remove any 
	 * temporary buffs that it has.
	 */
	@Override
	public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
		this.extraAttackUntilTurnEnd_ = 0;
		return boardModel;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("armor", this.armor_);
		json.put("weaponCharge", this.weaponCharge_);
		return json;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		}
		Hero hero = (Hero)other;
		if (weaponCharge_ != hero.getWeaponCharge()) return false;
		if (armor_ != hero.getArmor()) return false;
		
		return true;
	}
	
    @Override
    public int hashCode() {
    	int hash = super.hashCode();
    	hash = 31 * hash + (int) weaponCharge_;
    	hash = 31 * hash + (int) armor_;
    	return hash;
    }
}
