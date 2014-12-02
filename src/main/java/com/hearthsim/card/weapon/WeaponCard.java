package com.hearthsim.card.weapon;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class WeaponCard extends Card {

	byte weaponCharge_;
	byte weaponDamage_;
	
	public WeaponCard(String name, byte mana, byte weaponDamage, byte weaponCharge, boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed, true);
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

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) weaponCharge_;
        result = 31 * result + (int) weaponDamage_;
        return result;
    }

    /**
	 * 
	 * Use the card on the given target
	 * 
	 * This is the core implementation of card's ability
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode use_core(
			PlayerSide side,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (this.hasBeenUsed()) {
			//Card is already used, nothing to do
			return null;
		}

		if (isWaitingPlayer(side) || isNotHero(targetMinion)) {
			return null;
		}
		
		HearthTreeNode toRet = boardState;
		if (toRet != null) {
			toRet.data_.getCurrentPlayerHero().setAttack(this.weaponDamage_);
			toRet.data_.getCurrentPlayerHero().setWeaponCharge(this.weaponCharge_);
			this.hasBeenUsed(true);

		}
		
		return super.use_core(side, targetMinion, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
	}
}
