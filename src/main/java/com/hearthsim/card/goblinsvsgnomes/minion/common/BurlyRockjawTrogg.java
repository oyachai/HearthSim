package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class BurlyRockjawTrogg extends Minion implements CardPlayBeginInterface {

    public BurlyRockjawTrogg() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard,
                                          HearthTreeNode boardState) {
        if (cardUserPlayerSide != thisCardPlayerSide && usedCard instanceof SpellCard) {
            this.addAttack((byte)2);
        }
        return boardState;
    }
}
