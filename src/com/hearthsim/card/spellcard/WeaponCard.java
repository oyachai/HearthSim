package com.hearthsim.card.spellcard;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;

public class WeaponCard extends SpellCard {

	byte weaponCharge_;
	byte weaponDamage_;
	
	public WeaponCard(String name, byte mana, byte weaponDamage, byte weaponCharge, boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed);
		weaponCharge_ = weaponCharge;
		weaponDamage_ = weaponDamage;
	}

	@Override
	public Object deepCopy() {
		return new WeaponCard(this.getName(), this.getMana(), this.weaponDamage_, this.weaponCharge_, this.hasBeenUsed());
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!super.equals(other)) {
			return false;
		}
		
		if (other == null)
		{
		   return false;
		}
		
		if (this.getClass() != other.getClass())
		{
		   return false;
		}

		if (this.weaponDamage_ != ((WeaponCard)other).weaponDamage_) {
			return false;
		}

		if (this.weaponCharge_ != ((WeaponCard)other).weaponCharge_) {
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 * Use the weapon card
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		if (this.hasBeenUsed()) {
			//Card is already used, nothing to do
			return null;
		}
				
		if (playerIndex == 1 || minionIndex > 0) {
			return null;
		}
		
		HearthTreeNode toRet = boardState;
		if (toRet != null) {
			toRet.data_.getHero_p0().setAttack(this.weaponDamage_);
			toRet.data_.getHero_p0().setWeaponCharge(this.weaponCharge_);
			this.hasBeenUsed(true);

		}
		
		return super.use_core(thisCardIndex, playerIndex, minionIndex, toRet, deckPlayer0, deckPlayer1);
	}
}
