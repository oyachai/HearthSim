package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.EffectMinionAction;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Sap extends SpellCard {

    private final static EffectMinionAction<Card> effect = EffectMinionAction.BOUNCE;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Sap(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Sap() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.ENEMY_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Return an enemy minion to its hand
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState, boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
        if (toRet != null) {
            Sap.effect.applyEffect(PlayerSide.CURRENT_PLAYER, this, side, targetMinion, boardState);
        }
        return toRet;
    }
}
