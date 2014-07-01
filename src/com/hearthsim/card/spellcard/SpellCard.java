package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.util.BoardState;
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

	@Override
	public BoardState useOn(int playerIndex, int minionIndex, BoardState boardState) {
		boardState.setMana_p0(boardState.getMana_p0() - this.mana_);
		return boardState;
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellCard");
		return json;
	}
}
