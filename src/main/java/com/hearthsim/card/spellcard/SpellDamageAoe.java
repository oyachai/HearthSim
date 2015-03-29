package com.hearthsim.card.spellcard;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectAoeInterface;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

@Deprecated
public class SpellDamageAoe extends SpellDamage implements CardEffectAoeInterface {

    protected MinionFilter hitsFilter;

    public SpellDamageAoe() {
        super();
        this.hitsFilter = MinionFilter.ENEMY_MINIONS;
    }

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public SpellDamageAoe(byte mana, byte damage, boolean hasBeenUsed) {
        super(mana, damage, hasBeenUsed);
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        if (isCurrentPlayer(playerSide)) {
            return false;
        }

        if (isNotHero(minion)) {
            return false;
        }

        return true;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public MinionFilter getAoeFilter() {
        return this.hitsFilter;
    }
}
