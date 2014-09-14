package com.hearthsim.card.spellcard;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public class SpellDamage extends SpellCard {

	protected byte damage_;
	
	public SpellDamage(String name, byte mana, byte damage, boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed);
		damage_ = damage;
	}

	public SpellDamage() {
		super("Damage Spell", (byte)0, false);
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
		
		if (other == null) {
			return false;
		}

		if (this.getClass() != other.getClass()) {
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
		return new SpellDamage(this.getName(), this.getMana(), damage_, this.hasBeenUsed());
	}

	/**
	 * 
	 * Attack using this spell
	 * 
	 *
     * @param targetMinionPlayerModel
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck of player0
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode attack(
			PlayerModel targetMinionPlayerModel,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSException
	{
		return targetMinion.takeDamage(damage_, boardState.data_.getCurrentPlayer(), targetMinionPlayerModel, boardState, deckPlayer0, deckPlayer1, true, false);
 	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This is the core implementation of card's ability
	 * 
	 *
     * @param playerModel
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode use_core(
			PlayerModel playerModel,
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
				
		this.hasBeenUsed(true);
		HearthTreeNode toRet = boardState;

		toRet = this.attack(playerModel, targetMinion, toRet, deckPlayer0, deckPlayer1);
		toRet.data_.setMana_p0(toRet.data_.getMana_p0() - this.mana_);
		toRet.data_.removeCard_hand(this);
		
		return toRet;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellDamage");
		return json;
	}
}
