package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import org.json.JSONObject;



public class SpellCard extends Card {

	public SpellCard(String name, byte mana, boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed, true);
	}

	public SpellCard(String name, byte mana) {
		this(name, mana, false);
	}

    @Override
    public boolean canBeUsedOn(int playerIndex, Minion minion) {
        return !hasBeenUsed_ && !minion.getStealthed();
    }

	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellCard");
		return json;
	}
}
