package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TwistingNether extends SpellCard {

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public TwistingNether() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card destroys an enemy minion.
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
                    for (Minion minion : boardState.data_.getAllMinions()) {
                        minion.setHealth((byte)-99);
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
