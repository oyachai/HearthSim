package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdBlood extends SpellTargetableCard {

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public ColdBlood(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public ColdBlood() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return CharacterFilterTargetedSpell.ALL_MINIONS;
    }

    /**
     *
     * Use the card on the given target
     *
     * Give a minion +2 Attack. Combo: +4 Attack instead.
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
                    byte buffAmount = boardState.data_.getCurrentPlayer().isComboEnabled() ? (byte)4 : (byte)2;
                    targetCharacter.addAttack(buffAmount);
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
