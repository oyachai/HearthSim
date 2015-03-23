package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

import org.json.JSONObject;

public abstract class SpellCard extends Card {

    protected MinionFilter minionFilter = MinionFilterTargetedSpell.ALL;

    public SpellCard() {
        super();
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
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return this.minionFilter.targetMatches(PlayerSide.CURRENT_PLAYER, this, playerSide, minion, boardModel);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("type", "SpellCard");
        return json;
    }
}
