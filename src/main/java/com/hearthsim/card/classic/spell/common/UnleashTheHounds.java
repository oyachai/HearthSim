package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.classic.minion.common.Hound;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;

public class UnleashTheHounds extends SpellTargetableCard {

    @Deprecated
    public UnleashTheHounds(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    public UnleashTheHounds() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.SELF;
    }

    /**
     *
     * Use the card on the given target
     *
     * @param side
     * @param targetMinion
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param singleRealizationOnly
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    public EffectCharacter getTargetableEffect() {
        if (this.effect == null) {
            this.effect = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
                PlayerModel currentPlayer = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
                PlayerModel waitingPlayer = boardState.data_.modelForSide(PlayerSide.WAITING_PLAYER);
                int numHoundsToSummon = waitingPlayer.getNumMinions();
                if (numHoundsToSummon + currentPlayer.getNumMinions() > 7)
                    numHoundsToSummon = 7 - currentPlayer.getNumMinions();
                for (int indx = 0; indx < numHoundsToSummon; ++indx) {
                    boardState = new Hound().summonMinionAtEnd(PlayerSide.CURRENT_PLAYER, boardState, false, false);
                }
                return boardState;
            };
        }
        return this.effect;
    }
}
