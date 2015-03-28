package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterTargetedSpell;
import org.json.JSONObject;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TheCoin extends SpellCard {

    @Deprecated
    public TheCoin(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    public TheCoin() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.SELF;
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
    protected EffectMinionAction getEffect() {
        if (this.effect == null) {
            this.effect = new EffectMinionAction() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
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
