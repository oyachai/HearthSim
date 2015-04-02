package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Swipe extends SpellDamage {

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
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL_ENEMIES;
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
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {

        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, singleRealizationOnly);
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
