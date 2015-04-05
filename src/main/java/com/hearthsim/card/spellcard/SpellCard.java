package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import org.json.JSONObject;

public abstract class SpellCard extends Card {

    public SpellCard() {
        super();
    }

    @Deprecated
    public SpellCard(byte mana, boolean hasBeenUsed) {
        super(mana, hasBeenUsed, true);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("type", "SpellCard");
        return json;
    }
}
