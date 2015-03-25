package com.hearthsim.event;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectMinionSpellDamage<T extends SpellDamage> extends EffectMinionAction<T> {
    private byte damage;

    public EffectMinionSpellDamage(int damage) {
        this.damage = (byte) damage;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return targetCharacter.takeDamageAndNotify(this.damage, PlayerSide.CURRENT_PLAYER, targetSide, boardState, true, false);
    }
}
