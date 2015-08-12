package com.hearthsim.card.curseofnaxxramas.minion.epic;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShadeOfNaxxramas extends Minion {

    private final static EffectCharacter<Minion> effect = new EffectCharacterBuffDelta<>(1, 1);

    public ShadeOfNaxxramas() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = boardModel;
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            toRet = ShadeOfNaxxramas.effect.applyEffect(thisMinionPlayerIndex, this, toRet);
        }
        return super.startTurn(thisMinionPlayerIndex, toRet);
    }
}
