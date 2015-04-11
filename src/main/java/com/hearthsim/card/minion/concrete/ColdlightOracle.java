package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightOracle extends Minion implements MinionBattlecryInterface {

    public ColdlightOracle() {
        super();
    }

    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return new CardEffectCharacter<Minion>() {
            @Override
            public HearthTreeNode applyEffect(PlayerSide originSide, Minion origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                HearthTreeNode toRet = boardState;
                if (toRet instanceof CardDrawNode)
                    ((CardDrawNode) toRet).addNumCardsToDraw(2);
                else
                    toRet = new CardDrawNode(toRet, 2); //draw two cards

                toRet.data_.drawCardFromWaitingPlayerDeck(2);
                return toRet;
            }
        };
    }
}
