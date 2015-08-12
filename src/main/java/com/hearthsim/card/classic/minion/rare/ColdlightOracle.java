package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class ColdlightOracle extends Minion implements MinionBattlecryInterface {

    public ColdlightOracle() {
        super();
    }

    @Override
    public EffectCharacter getBattlecryEffect() {
        return (targetSide, targetCharacterIndex, boardState) -> {
            HearthTreeNode toRet = boardState;
            if (toRet instanceof CardDrawNode)
                ((CardDrawNode) toRet).addNumCardsToDraw(2);
            else
                toRet = new CardDrawNode(toRet, 2); //draw two cards

            toRet.data_.drawCardFromWaitingPlayerDeck(2);
            return toRet;
        };
    }
}
