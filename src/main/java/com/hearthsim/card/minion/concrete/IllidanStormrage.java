package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.CardPlayBeginInterface;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
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
                                          HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1,
                                          boolean singleRealizationOnly) throws HSException {
        HearthTreeNode toRet = boardState;
        PlayerModel targetPlayer = boardState.data_.modelForSide(thisCardPlayerSide);
        if (thisCardPlayerSide == cardUserPlayerSide && usedCard != this && !targetPlayer.isBoardFull()) {
            toRet = new FlameOfAzzinoth().summonMinion(thisCardPlayerSide, this, toRet, deckPlayer0, deckPlayer1, false, singleRealizationOnly);
        }
        return toRet;
    }
}
