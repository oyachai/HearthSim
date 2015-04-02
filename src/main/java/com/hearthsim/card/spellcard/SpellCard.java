package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectTargetableInterface;
import org.json.JSONObject;

public abstract class SpellCard extends Card implements CardEffectTargetableInterface {

    protected CardEffectCharacter effect;

    public SpellCard() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL;
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
