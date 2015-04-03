package com.hearthsim.card.minion.concrete;

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
                                          HearthTreeNode boardState, boolean singleRealizationOnly) {
        if (cardUserPlayerSide == thisCardPlayerSide && usedCard instanceof SpellCard) {
            this.addAttack((byte)1);
        }
        return boardState;
    }
}
