package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ManaWyrm extends Minion implements CardPlayBeginInterface {

    public ManaWyrm() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard,
                                          HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }
        if (cardUserPlayerSide == thisCardPlayerSide && usedCard instanceof SpellCard) {
            this.addAttack((byte)1);
        }
        return boardState;
    }
}
