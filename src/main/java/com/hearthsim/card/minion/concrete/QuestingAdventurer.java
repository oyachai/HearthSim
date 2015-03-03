package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class QuestingAdventurer extends Minion implements CardPlayBeginInterface {

    private static final boolean HERO_TARGETABLE = true;

    public QuestingAdventurer() {
        super();
        heroTargetable_ = HERO_TARGETABLE;

    }

    @Override
    public HearthTreeNode onCardPlayBegin(
            PlayerSide thisCardPlayerSide,
            PlayerSide cardUserPlayerSide,
            Card usedCard,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly)
    throws HSException {
        HearthTreeNode toRet = boardState;
        if (usedCard != this && thisCardPlayerSide == cardUserPlayerSide) {
            this.addAttack((byte)1);
            this.addHealth((byte)1);
            this.addMaxHealth((byte)1);
        }
        return toRet;
    }

}
