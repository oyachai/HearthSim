package com.hearthsim.card.minion;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthTreeNode;
import com.json.JSONObject;

public class Hero extends Minion {

	protected byte weaponCharge_;
	protected byte armor_;
	
	protected byte temporaryAttackDamage_;
	
	public Hero() {
		this("", (byte)30);
	}

	public Hero(String name, byte health) {
		this(name, (byte)0, health, (byte)0, (byte)0, false, false, false, false, (byte)0, false);
	}
	
	public Hero(
			String name,
			byte attack,
			byte health,
			byte armor,
			byte weaponCharge,
			boolean windFury,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			boolean frozen,
			byte temporaryAttackDamage,
			boolean hasBeenUsed) {
	
		super(name, (byte)0, attack, health, (byte)0, (byte)30, (byte)30, false, false, windFury, false, hasAttacked, hasWindFuryAttacked, frozen, false, hasBeenUsed);
		armor_ = armor;
		weaponCharge_ = weaponCharge;
		temporaryAttackDamage_ = temporaryAttackDamage;
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
	
	public byte getTemporaryAttackDamage() {
		return temporaryAttackDamage_;
	}
	
	public void setTemporaryAttackDamage(byte value) {
		temporaryAttackDamage_ = value;
		attack_ += temporaryAttackDamage_;
	}
	
	@Override
	public DeepCopyable deepCopy() {
		return new Hero(
				this.name_, 
				this.attack_,
				this.health_,
				this.armor_,
				this.weaponCharge_,
				this.windFury_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.temporaryAttackDamage_,
				this.hasBeenUsed_
				);
	}
	
	
	/**
	 * 
	 * Attack with the hero
	 * 
	 * A hero can only attack if it has a temporary buff, such as weapons
	 * 
	 * @param thisMinionIndex Attacking minion's index (note: attacking player index is assumed to be 0)
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode<BoardState> attack(
			int thisMinionIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		
		if (attack_ == 0) {
			return null;
		}

		if (this.weaponCharge_ == 0 && this.temporaryAttackDamage_ == 0) {
			return null;
		}
		
		//this is somewhat redundant, but it must be done here...
		if (frozen_) {
			this.hasAttacked_ = true;
			this.frozen_ = false;
			return boardState;
		}
		
		
		HearthTreeNode<BoardState> toRet = super.attack(thisMinionIndex, playerIndex, minionIndex, boardState, deck);
		
		if (toRet != null) {
			if (this.weaponCharge_ > 0) {
				this.weaponCharge_ -= 1;
				if (this.weaponCharge_ == 0) {
					this.attack_ = 0;
				}
			}
		}
		return toRet;
	}

	
	public HearthTreeNode<BoardState> useHeroAbility(int targetPlayerIndex, int targetMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
		return null;
	}
	
	/**
	 * Called when this minion takes damage
	 * 
	 * Overridden from Minion.  Need to handle armor.
	 * 
	 * @param damage The amount of damage to take
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public void takeDamage(byte damage, int thisPlayerIndex, int thisMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) throws HSInvalidPlayerIndexException {

		byte damageRemaining = (byte)(damage - armor_);
		if (damageRemaining > 0) {
			armor_ = 0;
			super.takeDamage(damageRemaining, thisPlayerIndex, thisMinionIndex, boardState, deck);
		} else {
			armor_ = (byte)(armor_ - damage);
			super.takeDamage((byte)0, thisPlayerIndex, thisMinionIndex, boardState, deck);
		}		
	}
	
	
	/**
	 * End the turn and resets the card state
	 * 
	 * This function is called at the end of the turn.  Any derived class must override it and remove any 
	 * temporary buffs that it has.
	 */
	@Override
	public BoardState endTurn(BoardState boardState, Deck deck) {
		if (this.temporaryAttackDamage_ > 0) {
			this.attack_ -= this.temporaryAttackDamage_;
			this.temporaryAttackDamage_ = 0;
		}
		return boardState;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Hero");
		return json;
	}
}
