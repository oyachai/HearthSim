package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.classic.minion.common.FlameOfAzzinoth;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class IllidanStormrage extends Minion implements CardPlayBeginInterface {

    public IllidanStormrage() {
        super();
    }

    @Override
    public HearthTreeNode onCardPlayBegin(PlayerSide thisCardPlayerSide,
                                          PlayerSide cardUserPlayerSide, Card usedCard,
                                          HearthTreeNode boardState) {
        if (this.setInHand()) {
            return boardState;
        }

        HearthTreeNode toRet = boardState;
        PlayerModel targetPlayer = boardState.data_.modelForSide(thisCardPlayerSide);
        if (thisCardPlayerSide == cardUserPlayerSide && usedCard != this && !targetPlayer.isBoardFull()) {
            toRet = new FlameOfAzzinoth().summonMinion(thisCardPlayerSide, this, toRet, false);
        }
        return toRet;
    }
}
