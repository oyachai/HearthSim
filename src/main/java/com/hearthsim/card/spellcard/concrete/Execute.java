package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Execute extends SpellCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Execute(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Execute() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.ENEMY_MINIONS;
    }

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        if (!super.canBeUsedOn(playerSide, minion, boardModel)) {
            return false;
        }

        if (minion.getHealth() == minion.getMaxHealth()) {
            return false;
        }

        return true;
    }

    /**
     *
     * Use the card on the given target
     *
     * Destroy a damaged minion.
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected CardEffectCharacter getEffect() {
        return CardEffectCharacter.DESTROY;
    }
}
