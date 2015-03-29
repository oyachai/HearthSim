package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class WildGrowth extends SpellCard {


    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public WildGrowth(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public WildGrowth() {
        super();

        this.characterFilter = CharacterFilterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * Gain an empty mana crystal (i.e., it increases maxMana by 1).  If maxMana is already 10, then it places
     * the ExcessMana card in your hand.
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
        if (this.effect == null) {
            this.effect = new CardEffectCharacter() {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
                    PlayerModel player = boardState.data_.modelForSide(targetSide);
                    if (player.getMaxMana() >= 10) {
                        player.placeCardHand(new ExcessMana());
                    } else {
                        player.addMaxMana((byte) 1);
                    }
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
