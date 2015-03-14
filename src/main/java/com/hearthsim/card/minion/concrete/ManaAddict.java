package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ManaAddict extends Minion implements CardPlayBeginInterface {

    public ManaAddict() {
        super();
    }

    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard,
            HearthTreeNode boardState, boolean singleRealizationOnly)
            throws HSException {
        if (cardUserPlayerSide == thisCardPlayerSide && usedCard instanceof SpellCard) {
            this.addExtraAttackUntilTurnEnd((byte)2);
        }
        return boardState;
    }
}
