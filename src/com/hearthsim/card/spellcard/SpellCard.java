package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.HearthTreeNode;
import com.json.JSONObject;



public class SpellCard extends Card {

	public SpellCard(String name, byte mana, boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed, true);
	}

	public SpellCard(String name, byte mana) {
		this(name, mana, false);
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

	return true;
	}

	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This is an abstract function, at least conceptually.
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode<BoardState> use_core(
			int thisCardIndex,
			int playerIndex,
			int minionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSInvalidPlayerIndexException
	{
		boardState.data_.setMana_p0(boardState.data_.getMana_p0() - this.mana_);
		boardState.data_.removeCard_hand(thisCardIndex);
		return boardState;
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellCard");
		return json;
	}
}
