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
	
	protected boolean hasTemporaryAttack_;
	
	public Hero() {
		this("", (byte)30);
	}

	public Hero(String name, byte health) {
		this(name, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false, false);
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
			boolean hasTemporaryAttack,
			boolean hasBeenUsed) {
	
		super(name, (byte)0, attack, health, (byte)0, (byte)30, (byte)30, false, false, windFury, false, hasAttacked, hasWindFuryAttacked, frozen, false, hasBeenUsed);
		armor_ = armor;
		weaponCharge_ = weaponCharge;
		hasTemporaryAttack_ = hasTemporaryAttack;
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
	
	public boolean hasTemporaryAttack() {
		return hasTemporaryAttack_;
	}
	
	public void hasTemporaryAttack(boolean value) {
		hasTemporaryAttack_ = value;
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
				this.hasTemporaryAttack_,
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
		
		//this is somewhat redundant, but it must be done here...
		if (frozen_) {
			this.hasAttacked_ = true;
			this.frozen_ = false;
			return boardState;
		}
		
		HearthTreeNode<BoardState> toRet = super.attack(thisMinionIndex, playerIndex, minionIndex, boardState, deck);
		
		if (toRet != null) {
			if (this.weaponCharge_ > 0)
				this.weaponCharge_ -= 1;
			if (this.hasTemporaryAttack_)
				this.hasTemporaryAttack_ = false;
		}
		return toRet;
	}

	
	public HearthTreeNode<BoardState> useHeroAbility(int targetPlayerIndex, int targetMinionIndex, HearthTreeNode<BoardState> boardState, Deck deck) {
		return null;
	}
	
	


	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Hero");
		return json;
	}
}
