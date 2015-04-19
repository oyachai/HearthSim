package com.hearthsim.card.classic.spell.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamageSpell;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Savagery extends SpellTargetableCard {

    public Savagery() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }


    @Override
    public EffectCharacter getTargetableEffect() {
        return new EffectCharacter() {
            @Override
            public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
                EffectCharacter<Card> effect = new EffectCharacterDamageSpell<>(boardState.data_.getCharacter(originSide, 0).getTotalAttack());
                return effect.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
            }
        };
    }
}
