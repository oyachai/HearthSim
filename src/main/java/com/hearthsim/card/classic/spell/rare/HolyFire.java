package com.hearthsim.card.classic.spell.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HolyFire extends SpellDamageTargetableCard {

    private static final FilterCharacter filter = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() {
            return true;
        }

        @Override
        protected boolean includeEnemyMinions() {
            return true;
        }

        @Override
        protected boolean includeOwnMinions() {
            return true;
        }
    };

    public HolyFire() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return HolyFire.filter;
    }

    @Deprecated
    public HolyFire(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     *
     * Use the card on the given target
     *
     * Deals 5 damage and heals the hero for 5.
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
            HearthTreeNode boardState)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);
        if (toRet != null) {
            toRet.data_.getCurrentPlayer().getHero().takeHealAndNotify((byte) 5, PlayerSide.CURRENT_PLAYER, toRet);
        }
        return toRet;
    }
}
