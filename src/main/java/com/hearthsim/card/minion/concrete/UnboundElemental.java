package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class UnboundElemental extends Minion implements CardPlayBeginInterface {

    public UnboundElemental() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard, HearthTreeNode boardState, boolean singleRealizationOnly) throws HSException {
        if (usedCard != this && thisCardPlayerSide == cardUserPlayerSide && usedCard.triggersOverload()) {
            this.addAttack((byte)1);
            this.addHealth((byte)1);
            this.addMaxHealth((byte)1);
        }
        return boardState;
    }
}
