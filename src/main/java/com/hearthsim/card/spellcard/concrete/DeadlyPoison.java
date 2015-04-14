package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class DeadlyPoison extends SpellTargetableCard {

    private final static EffectCharacter effect = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Hero hero = (Hero)boardState.data_.getCharacter(targetSide, targetCharacterIndex);
        hero.setAttack((byte)(hero.getAttack() + 2));
        return boardState;
    };

    private final static FilterCharacter filter = new FilterCharacterTargetedSpell() {
        @Override
        protected boolean includeOwnHero() {
            return true;
        }

        @Override
        public boolean targetMatches(PlayerSide originSide, Card origin, PlayerSide targetSide, Minion targetCharacter, BoardModel board) {
            if (!super.targetMatches(originSide, origin, targetSide, targetCharacter, board)) {
                return false;
            }

            if (((Hero)targetCharacter).getWeapon() == null) {
                return false;
            }

            return true;
        }
    };

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public DeadlyPoison(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public DeadlyPoison() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return DeadlyPoison.filter;
    }

    /**
     *
     * Give your weapon +2 attack
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        return DeadlyPoison.effect;
    }
}
