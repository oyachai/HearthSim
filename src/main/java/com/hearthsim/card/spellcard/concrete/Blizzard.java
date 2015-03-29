package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellAoeInterface;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.card.spellcard.SpellDamageAoe;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.event.MinionFilterTargetedSpell;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.SpellEffectCharacterDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Blizzard extends SpellDamage implements SpellAoeInterface {

    /*
     * Deal $2 damage to all enemy minions and <b>Freeze</b> them.
     */
    public Blizzard() {
        super();
        this.minionFilter = MinionFilterTargetedSpell.OPPONENT;
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

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getEffect(); }

    @Override
    public MinionFilter getAoeFilter() {
        return MinionFilter.ENEMY_MINIONS;
    }
}
