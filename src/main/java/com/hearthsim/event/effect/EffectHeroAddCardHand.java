package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EffectHeroAddCardHand<T extends Card> implements EffectHero<T> {
    private static final Logger logger = LoggerFactory.getLogger(EffectHeroAddCardHand.class);

    private Class<? extends Card> cardClass;

    public EffectHeroAddCardHand(Class<? extends Card> cardClass) {
        this.cardClass = cardClass;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, HearthTreeNode boardState) {
        if (boardState.data_.modelForSide(targetSide).isHandFull()) {
            return boardState;
        }
        try {
            Card newCard = this.cardClass.newInstance();
            boardState.data_.placeCardHand(targetSide, newCard);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Cannot instantiate the card: ", e);
        }

        return boardState;
    }
}
