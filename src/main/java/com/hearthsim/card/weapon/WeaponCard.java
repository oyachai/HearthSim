package com.hearthsim.card.weapon;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class WeaponCard extends Card {

	byte weaponCharge_;
	byte weaponDamage_;

    public WeaponCard(){
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        weaponCharge_ = (byte) implementedCard.durability;
        weaponDamage_ = (byte) implementedCard.attack_;
        name_ = implementedCard.name_;
        baseManaCost = (byte) implementedCard.mana_;
        isInHand_ = true;
    }

	@Override
	public Card deepCopy() {
        WeaponCard weapon = null;
        try {
            weapon = getClass().newInstance();
        } catch (InstantiationException e) {
            log.error("instantiation error", e);
        } catch (IllegalAccessException e) {
            log.error("illegal access error", e);
        }
        if (weapon == null) {
            throw new RuntimeException("unable to instantiate weapon.");
        }
        weapon.name_ = name_;
        weapon.baseManaCost = baseManaCost;
        weapon.weaponCharge_ = weaponCharge_;
        weapon.weaponDamage_ = weaponDamage_;
        weapon.hasBeenUsed = hasBeenUsed;

        return weapon;
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
        result = 31 * result + weaponCharge_;
        result = 31 * result + weaponDamage_;
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
	@Override
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
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
            toRet.data_.getCurrentPlayerHero().setWeapon(this);
			this.hasBeenUsed(true);
		}
		
		return super.use_core(side, targetMinion, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
	}

    public byte getWeaponCharge_() {
        return weaponCharge_;
    }

    public void setWeaponCharge_(byte weaponCharge_) {
        this.weaponCharge_ = weaponCharge_;
    }

    public byte getWeaponDamage_() {
        return weaponDamage_;
    }

    public void setWeaponDamage_(byte weaponDamage_) {
        this.weaponDamage_ = weaponDamage_;
    }

    public void onAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {

    }

    public void minionSummonedEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) {

    }
}
