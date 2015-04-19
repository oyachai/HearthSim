package com.hearthsim.card.classic.spell.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.card.spellcard.SpellDamageTargetableCard;
import com.hearthsim.event.effect.EffectCharacterDamageSpell;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class IceLance extends SpellDamageTargetableCard {

    public IceLance() {
        super();
    }

    /**
     * <b>Freeze</b> a character. If it was already <b>Frozen</b>, deal $4 damage instead.
     */
    @Override
    public EffectCharacterDamageSpell<SpellDamage> getTargetableEffect() {
        if (this.effect == null) {
            this.effect = new EffectCharacterDamageSpell<SpellDamage>(damage_) {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, SpellDamage origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
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
