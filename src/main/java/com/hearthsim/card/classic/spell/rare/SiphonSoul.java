package com.hearthsim.card.classic.spell.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterHeal;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class SiphonSoul extends SpellTargetableCard {

    private static final EffectCharacter<Card> heal = new EffectCharacterHeal<>(3);

    private static final EffectCharacter effect = new EffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            boardState = EffectCharacter.DESTROY.applyEffect(originSide, origin, targetSide, targetCharacterIndex, boardState);
            boardState = SiphonSoul.heal.applyEffect(originSide, origin, originSide, 0, boardState);
            return boardState;
        }
    };

    public SiphonSoul() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacterTargetedSpell.ALL_MINIONS;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return SiphonSoul.effect;
    }
}
