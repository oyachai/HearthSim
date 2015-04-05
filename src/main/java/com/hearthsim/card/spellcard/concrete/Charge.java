package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Charge extends SpellTargetableCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public Charge(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Charge() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.FRIENDLY_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * This card gives the target +2 attack and Charge.
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public CardEffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    targetCharacter.setAttack((byte)(targetCharacter.getAttack() + 2));
                    targetCharacter.setCharge(true);
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
