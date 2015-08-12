package com.hearthsim.card.classic.spell.epic;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectCharacterDamageSpell;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.event.filter.FilterCharacterTargetedSpell;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShieldSlam extends SpellTargetableCard {

    public ShieldSlam() {
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
            public HearthTreeNode applyEffect(PlayerSide targetSide, CharacterIndex targetCharacterIndex, HearthTreeNode boardState) {
                Hero hero = boardState.data_.modelForSide(PlayerSide.CURRENT_PLAYER).getHero();
                EffectCharacter<Card> effect = new EffectCharacterDamageSpell<>(hero.getArmor());
                return effect.applyEffect(targetSide, targetCharacterIndex, boardState);
            }
        };
    }
}
