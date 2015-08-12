package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MicroMachine extends Minion {

    private final static EffectCharacter<Minion> effect = new EffectCharacterBuffDelta<>(1, 0);

    public MicroMachine() {
        super();
    }

    @Override
    public HearthTreeNode startTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode toRet = MicroMachine.effect.applyEffect(thisMinionPlayerIndex, this, boardModel);
        return super.startTurn(thisMinionPlayerIndex, toRet);
    }
}
