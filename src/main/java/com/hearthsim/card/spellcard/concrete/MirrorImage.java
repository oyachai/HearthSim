package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.MirrorImageMinion;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;

public class MirrorImage extends SpellTargetableCard {


    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    @Deprecated
    public MirrorImage(boolean hasBeenUsed) {
        this();
        this.hasBeenUsed = hasBeenUsed;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public MirrorImage() {
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
     * Summons 2 mirror images
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
        if (this.effect == null) {
            this.effect = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
                PlayerModel currentPlayer = boardState.data_.modelForSide(targetSide);
                if (currentPlayer.isBoardFull()) {
                    return null;
                }

                Minion mi0 = new MirrorImageMinion();
                boardState = mi0.summonMinionAtEnd(targetSide, boardState, false, false);

                if (!currentPlayer.isBoardFull()) {
                    Minion mi1 = new MirrorImageMinion();
                    boardState = mi1.summonMinionAtEnd(targetSide, boardState, false, false);
                }
                return boardState;
            };
        }
        return this.effect;
    }
}
