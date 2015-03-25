package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.EffectMinionSpellDamage;
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
    protected EffectMinionSpellDamage<SpellDamage> getEffect() {
        if (this.effect == null) {
            this.effect = new EffectMinionSpellDamage<SpellDamage>(damage_) {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, SpellDamage origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) throws HSException {
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    targetCharacter.setFrozen(true);
                    return super.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
                }
            };
        }
        return this.effect;
    }
}
