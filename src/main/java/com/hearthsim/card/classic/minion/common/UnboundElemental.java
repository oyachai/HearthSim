package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class UnboundElemental extends Minion implements CardPlayBeginInterface {

    private final static EffectCharacter<Card> effect = new EffectCharacterBuffDelta<>(1, 1);

    public UnboundElemental() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }
        if (usedCard != this && thisCardPlayerSide == cardUserPlayerSide && usedCard.triggersOverload()) {
            boardState = UnboundElemental.effect.applyEffect(thisCardPlayerSide, this, boardState);
        }
        return boardState;
    }
}
