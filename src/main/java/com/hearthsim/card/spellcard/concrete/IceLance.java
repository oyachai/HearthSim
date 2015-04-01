package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.effect.SpellEffectCharacterDamage;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class IceLance extends SpellDamage {

    public IceLance() {
        super();
    }

    /**
     * <b>Freeze</b> a character. If it was already <b>Frozen</b>, deal $4 damage instead.
     */
    @Override
    public SpellEffectCharacterDamage getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new SpellEffectCharacterDamage(damage_) {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    HearthTreeNode toRet = boardState;
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    if (targetCharacter.getFrozen()) {
                        toRet = super.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
                    } else {
                        targetCharacter.setFrozen(true);
                    }
                    return toRet;
                }
            };
        }
        return this.effect;
    }
}
