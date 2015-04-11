package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterDraw;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class GadgetzanAuctioneer extends Minion implements CardPlayBeginInterface {

    private final static CardEffectCharacter<Card> effect = new CardEffectCharacterDraw<>(1);

    public GadgetzanAuctioneer() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState, boolean singleRealizationOnly) {
        if (!this.isInHand() && thisCardPlayerSide == cardUserPlayerSide && usedCard instanceof SpellCard) {
            return GadgetzanAuctioneer.effect.applyEffect(cardUserPlayerSide, usedCard, thisCardPlayerSide, this, boardState);
        }

        return boardState;
    }
}
