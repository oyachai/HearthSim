package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CobaltGuardian extends Minion implements CardPlayBeginInterface {

    public CobaltGuardian() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide, PlayerSide cardUserPlayerSide, Card usedCard,
                                          HearthTreeNode boardState) {
        if (cardUserPlayerSide != thisCardPlayerSide) {
            return boardState;
        }

        if (!(usedCard instanceof Minion)) {
            return boardState;
        }

        if (this.setInHand()) {
            return boardState;
        }

        if (usedCard == this) {
            return boardState;
        }

        if (((Minion) usedCard).getTribe() != MinionTribe.MECH) {
            return boardState;
        }

        this.setDivineShield(true);
        return boardState;
    }
}
