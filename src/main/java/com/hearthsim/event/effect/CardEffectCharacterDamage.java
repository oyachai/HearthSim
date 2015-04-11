package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardEffectCharacterDamage<T extends Card> implements CardEffectCharacter<T> {
    private final byte damage;
    private final boolean effectedBySpellpower;

    public CardEffectCharacterDamage(int damage) {
        this(damage, false);
    }

    protected CardEffectCharacterDamage(int damage, boolean effectedBySpellpower) {
        this.damage = (byte) damage;
        this.effectedBySpellpower = effectedBySpellpower;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, T origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
        Minion targetMinion = boardState.data_.modelForSide(targetSide).getCharacter(targetCharacterIndex);
        return targetMinion.takeDamageAndNotify(this.damage, PlayerSide.CURRENT_PLAYER, targetSide, boardState, this.effectedBySpellpower, true);
    }
}
