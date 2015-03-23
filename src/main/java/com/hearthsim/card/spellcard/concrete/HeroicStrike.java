package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HeroicStrike extends SpellCard {

    private static final byte DAMAGE_AMOUNT = 3;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public HeroicStrike(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public HeroicStrike() {
        super();

        this.minionFilter = MinionFilterTargetedSpell.SELF;
    }

    /**
     * Heroic Strike
     *
     * Gives the hero +4 attack this turn
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
            HearthTreeNode boardState,
            boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
        if (toRet != null) {
            toRet.data_.getCurrentPlayer().getHero().setExtraAttackUntilTurnEnd((byte)(DAMAGE_AMOUNT + toRet.data_.getCurrentPlayer().getHero().getExtraAttackUntilTurnEnd()));
        }

        return toRet;
    }
}
