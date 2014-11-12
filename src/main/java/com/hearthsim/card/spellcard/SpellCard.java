package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import org.json.JSONObject;

public class SpellCard extends Card {

	public SpellCard(byte mana, boolean hasBeenUsed) {
		super(mana, hasBeenUsed, true);
	}

	public SpellCard(byte mana) {
		this(mana, false);
	}

	@Override
	public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
		return !hasBeenUsed && !minion.getStealthed() && minion.isHeroTargetable();
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellCard");
		return json;
	}
}
