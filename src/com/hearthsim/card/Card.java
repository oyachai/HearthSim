package com.hearthsim.card;

import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.DeepCopyable;
import com.json.*;

public class Card implements DeepCopyable {

	/**
	 * Name of the card
	 */
	protected String name_;
	
	/**
	 * Mana cost of the card
	 */
	protected byte mana_;

	/**
	 * Hero class
	 * 
	 * 0 = none
	 * 1 = mage
	 * 2 = hunter
	 * 3 = paladin
	 * 4 = warrior
	 * 5 = druid
	 * 6 = warlock
	 * 7 = shaman
	 * 8 = priest
	 * 9 = rogue
	 */
	byte charClass_;
	
	protected boolean hasBeenUsed_;
	protected boolean isInHand_;

	public Card(String name, byte mana, boolean hasBeenUsed, boolean isInHand) {
		name_ = name;
		mana_ = mana;
		hasBeenUsed_ = hasBeenUsed;
		isInHand_ = isInHand;
	}

	public Card(String name, byte mana) {
		this(name, mana, true, true);
	}
	
	public String getName() {
		return name_;
	}
	

	public byte getMana() {
		return mana_;
	}
	
	public void setMana(byte mana) {
		mana_ = mana;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "Card");
		json.put("name", name_);
		json.put("mana", mana_);
		json.put("hasBeenUsed", hasBeenUsed_);
		return json;
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
	
	public boolean hasBeenUsed() {
		return hasBeenUsed_;
	}
	
	public void hasBeenUsed(boolean value) {
		hasBeenUsed_ = value;
	}
	
	
	public void isInHand(boolean value) {
		isInHand_ = value;
	}
	
	public boolean isInHand() {
		return isInHand_;
	}
	
	@Override
	public Object deepCopy() {
		return new Card(this.name_, this.mana_, this.hasBeenUsed_, this.isInHand_);
	}
	
	@Override
	public boolean equals(Object other)
	{
	   if (other == null)
	   {
	      return false;
	   }

	   if (this.getClass() != other.getClass())
	   {
	      return false;
	   }

	   // More logic here to be discuss below...
	   if (mana_ != ((Card)other).mana_)
		   return false;
	   
	   if (hasBeenUsed_ != ((Card)other).hasBeenUsed_)
		   return false;

	   if (isInHand_ != ((Card)other).isInHand_)
		   return false;
	   
	   if (!name_.equals(((Card)other).name_))
		   return false;

	   return true;
	}


	/**
	 * 
	 * Use the card on the given target
	 * 
	 * @param playerIndex
	 * @param minionIndex
	 * @param boardState
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public BoardState useOn(int playerIndex, int minionIndex, BoardState boardState) {
		
		//A generic card does nothing except for consuming mana
		boardState.setMana_p0(boardState.getMana_p0() - this.mana_);
		return boardState;
	}
	
}



