package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SpellEffectCharacterDamage extends CardEffectCharacter {
    private byte damage;

    public SpellEffectCharacterDamage(int damage) {
        this.damage = (byte) damage;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
        Minion targetCharacter = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return targetCharacter.takeDamageAndNotify(this.damage, PlayerSide.CURRENT_PLAYER, targetSide, boardState, true, false);
    }
}
