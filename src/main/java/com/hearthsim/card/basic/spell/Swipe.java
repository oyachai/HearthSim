package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Swipe extends SpellDamageTargetableCard {

    private static final byte DAMAGE_AMOUNT = 4;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Swipe(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     * Defaults to hasBeenUsed = false
     */
    public Swipe() {
        super();

        this.damage_ = Swipe.DAMAGE_AMOUNT;
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_ENEMIES;
    }

    /**
     * Use the card on the given target
     * Deals 4 damage to an enemy and 1 damage to all other enemies
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {

        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);
        PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        if (!targetMinion.isHero()) {
            toRet = waitingPlayer.getHero().takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, side, boardState, true, false);
        }

        for (Minion minion : waitingPlayer.getMinions()) {
            if (minion != targetMinion) {
                toRet = minion.takeDamageAndNotify((byte) 1, PlayerSide.CURRENT_PLAYER, side, toRet, true, false);
            }
        }

        return toRet;
    }
}
