package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectCharacterBuffDelta;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShadeOfNaxxramas extends Minion {

    private final static CardEffectCharacter effect = new CardEffectCharacterBuffDelta(1, 1);

    public ShadeOfNaxxramas() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            toRet = ShadeOfNaxxramas.effect.applyEffect(thisMinionPlayerIndex, null, thisMinionPlayerIndex, this, toRet);
        }
        return super.startTurn(thisMinionPlayerIndex, toRet);
    }
}
