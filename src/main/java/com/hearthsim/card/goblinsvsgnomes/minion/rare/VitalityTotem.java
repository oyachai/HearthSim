package com.hearthsim.card.goblinsvsgnomes.minion.rare;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class VitalityTotem extends Minion {

    private static final EffectCharacter<Minion> effect = new EffectCharacterHeal<>(4);

    public VitalityTotem() {
        super();
    }

    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel) throws HSException {
        if (thisMinionPlayerIndex == PlayerSide.CURRENT_PLAYER) {
            boardModel = VitalityTotem.effect.applyEffect(thisMinionPlayerIndex, CharacterIndex.HERO, boardModel);
        }
        return super.endTurn(thisMinionPlayerIndex, boardModel);
    }
}
