package com.hearthsim.card.spellcard;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.entity.BaseEntity;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public class SpellDamage extends SpellCard {

	protected byte damage_;
	
	public SpellDamage(byte mana, byte damage, boolean hasBeenUsed) {
		super(mana, hasBeenUsed);
		damage_ = damage;
	}

	public SpellDamage() {
		super((byte)0, false);
		damage_ = 0;
	}

	public byte getAttack() {
		return damage_;
	}
	
	@Override
	public boolean equals(Object other)
	{

		if (!super.equals(other)) {
			return false;
		}
		
		if (this.damage_ != ((SpellDamage)other).damage_) {
			return false;
		}

	   return true;
	}

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) damage_;
        return result;
    }

    @Override
	public Object deepCopy() {
		return new SpellDamage(this.getMana(), damage_, this.hasBeenUsed());
	}

	/**
	 * 
	 * Attack using this spell
	 * 
	 *
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode attack(
			PlayerSide targetMinionPlayerSide,
			BaseEntity targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		return targetMinion.takeDamage(damage_, PlayerSide.CURRENT_PLAYER, targetMinionPlayerSide, boardState, deckPlayer0, deckPlayer1, true, false);
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
				
		this.hasBeenUsed(true);
		HearthTreeNode toRet = boardState;

		toRet = this.attack(side, targetMinion, toRet, deckPlayer0, deckPlayer1);
		toRet.data_.getCurrentPlayer().subtractMana(this.mana_);
		toRet.data_.removeCard_hand(this);
		
		return toRet;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellDamage");
		return json;
	}
}
