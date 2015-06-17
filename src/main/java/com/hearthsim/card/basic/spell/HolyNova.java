package com.hearthsim.card.basic.spell;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectOnResolveAoe;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class HolyNova extends SpellDamage implements EffectOnResolveAoe {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public HolyNova(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     * Defaults to hasBeenUsed = false
     */
    public HolyNova() {
        super();
    }

    @Override
    public EffectCharacter getAoeEffect() {
        return this.getSpellDamageEffect();
    }

    @Override
    public FilterCharacter getAoeFilter() {
        return FilterCharacter.ALL_ENEMIES;
    }

    /**
     * Use the card on the given target
     * Deal 2 damage to all enemy characters and heal all friendly characters by 2
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(PlayerSide side, Minion targetMinion, HearthTreeNode boardState) throws HSException {

        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState);

        if (toRet != null) {
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);

            toRet = currentPlayer.getHero().takeHealAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, toRet);
            for (Minion minion : currentPlayer.getMinions()) {
                toRet = minion.takeHealAndNotify((byte) 2, PlayerSide.CURRENT_PLAYER, toRet);
            }
        }
        return toRet;
    }
}
