package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class QuestingAdventurer extends Minion implements CardPlayBeginInterface {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffDelta(1, 1);

    public QuestingAdventurer() {
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
            return QuestingAdventurer.effect.applyEffect(cardUserPlayerSide, usedCard, thisCardPlayerSide, this, boardState);
        }

        return boardState;
    }
}
