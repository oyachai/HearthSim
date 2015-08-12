package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterBuffDelta;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Gruul extends Minion {

    private final static EffectCharacter<Minion> effect = new EffectCharacterBuffDelta<>(1, 1);

    public Gruul() {
        super();
    }

    /**
     *
     * At the end of each turn, gain +1/+1
     *
     */
    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel);
        return Gruul.effect.applyEffect(thisMinionPlayerIndex, this, tmpState);
    }
}
