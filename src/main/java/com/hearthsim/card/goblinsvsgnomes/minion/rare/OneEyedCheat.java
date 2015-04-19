package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class OneEyedCheat extends Minion implements CardPlayBeginInterface {

    private final static EffectCharacter<Card> effect = (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
        Minion minion = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
        minion.setStealthed(true);
        return boardState;
    };

    public OneEyedCheat() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(
        PlayerSide thisCardPlayerSide,
        PlayerSide cardUserPlayerSide,
        Card usedCard,
        HearthTreeNode boardState,
        boolean singleRealizationOnly) {
        if (usedCard != this && !this.isInHand() && thisCardPlayerSide == cardUserPlayerSide) {
            return OneEyedCheat.effect.applyEffect(cardUserPlayerSide, usedCard, thisCardPlayerSide, this, boardState);
        }

        return boardState;
    }
}
