package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.effect.SpellEffectCharacterDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Blizzard extends SpellDamageAoe {

    /*
     * Deal $2 damage to all enemy minions and <b>Freeze</b> them.
     */
    public Blizzard() {
        super();
    }

    @Override
    protected SpellEffectCharacterDamage getEffect() {
        if (this.effect == null) {
            this.effect = new SpellEffectCharacterDamage(damage_) {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    targetCharacter.setFrozen(true);
                    return super.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
                }
            };
        }
        return this.effect;
    }
}
