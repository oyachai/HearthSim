package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.Minion.MinionTribe;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.CharacterFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SacrificialPact extends SpellCard {

    private final static CharacterFilter filter = new CharacterFilterTargetedSpell() {
        @Override
        protected boolean includeEnemyHero() { return true; }

        @Override
        protected boolean includeEnemyMinions() { return true; }

        @Override
        protected boolean includeOwnMinions() { return true; }

        @Override
        protected MinionTribe tribeFilter() { return MinionTribe.DEMON; }
    };

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public SacrificialPact() {
        super();
    }

    @Override
    public CharacterFilter getTargetableFilter() {
        return SacrificialPact.filter;
    }

    /**
     *
     * Use the card on the given target
     *
     * Gives a destroy a demon
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
                    boardState = boardState.data_.modelForSide(originSide).getHero().takeHealAndNotify((byte) 5, originSide, boardState);
                    targetCharacter.setHealth((byte) -99);
                    return boardState;
                }
            };
        }
        return this.effect;
    }
}
