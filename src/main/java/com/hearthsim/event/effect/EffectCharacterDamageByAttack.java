package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectCharacterDamageByAttack<T extends Card> implements EffectCharacter<T> {

    private final boolean effectedBySpellpower;

    public EffectCharacterDamageByAttack(boolean effectedBySpellpower) {
        this.effectedBySpellpower = effectedBySpellpower;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return targetMinion.takeDamageAndNotify(targetMinion.getTotalAttack(boardState, targetSide),
            PlayerSide.CURRENT_PLAYER, targetSide, boardState, this.effectedBySpellpower, true);
    }
}
