package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.event.effect.CardEffectTargetableInterface;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

import com.hearthsim.util.HearthAction;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode;
import org.json.JSONObject;

import java.util.Collection;

public abstract class SpellCard extends Card implements CardEffectTargetableInterface {

    protected CardEffectCharacter effect;

    public SpellCard() {
        super();
    }

    // some cards require data from import so we have to use lazy loading
    @Override
    public abstract CardEffectCharacter getTargetableEffect();

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL;
    }

    @Deprecated
    public SpellCard(byte mana, boolean hasBeenUsed) {
        super(mana, hasBeenUsed, true);
    }

    @Deprecated
    public SpellCard(byte mana) {
        this(mana, false);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("type", "SpellCard");
        return json;
    }
}
