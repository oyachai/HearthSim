package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.event.CharacterFilter;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.event.effect.CardEffectOnResolveAoeInterface;
import com.hearthsim.event.effect.SpellEffectCharacterDamage;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Blizzard extends SpellDamage implements CardEffectOnResolveAoeInterface {

    /*
     * Deal $2 damage to all enemy minions and <b>Freeze</b> them.
     */
    public Blizzard() {
        super();
    }

    @Override
    public SpellEffectCharacterDamage getSpellDamageEffect() {
        if (this.effect == null) {
            this.effect = new SpellEffectCharacterDamage(damage_) {
                @Override
                public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                    Minion targetCharacter = boardState.data_.getCharacter(targetSide, targetCharacterIndex);
                    targetCharacter.setFrozen(true);
                    return super.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
                }
            };
        }
        return this.effect;
    }

    @Override
    public CardEffectCharacter getAoeEffect() { return this.getSpellDamageEffect(); }

    @Override
    public CharacterFilter getAoeFilter() {
        return CharacterFilter.ENEMY_MINIONS;
    }
}
