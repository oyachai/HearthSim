package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public class TheCoin extends SpellTargetableCard {

    @Deprecated
    public TheCoin(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    public TheCoin() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public CardEffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    byte newMana = boardState.data_.getCurrentPlayer().getMana();
                    newMana = newMana >= 10 ? newMana : (byte)(newMana + 1);
                    boardState.data_.getCurrentPlayer().setMana(newMana);
                    return boardState;
                }
            };
        }
        return this.effect;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("type", "SpellTheCoin");
        return json;
    }
}
