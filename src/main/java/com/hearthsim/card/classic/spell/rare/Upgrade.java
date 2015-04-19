package com.hearthsim.card.classic.spell.rare;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.spellcard.SpellTargetableCard;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.card.classic.weapon.common.HeavyAxe;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.event.effect.EffectHero;
import com.hearthsim.event.effect.EffectHeroWeapon;
import com.hearthsim.event.effect.EffectHeroWeaponBuffDelta;
import com.hearthsim.event.filter.FilterCharacter;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Upgrade extends SpellTargetableCard {

    private final static EffectHero<Card> buff = new EffectHeroWeaponBuffDelta<>(1, 1);

    private final static EffectCharacter effect = new EffectCharacter() {
        @Override
        public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, int targetCharacterIndex, HearthTreeNode boardState) {
            Hero hero = boardState.data_.modelForSide(targetSide).getHero();
            WeaponCard weapon = hero.getWeapon();
            if (weapon != null) {
                boardState = Upgrade.buff.applyEffect(originSide, origin, targetSide, boardState);
            } else {
                EffectHero<Card> newWeapon = new EffectHeroWeapon<>(new HeavyAxe());
                boardState = newWeapon.applyEffect(originSide, origin, targetSide, boardState);
            }
            return boardState;
        }
    };

    public Upgrade() {
        super();
    }

    @Override
    public FilterCharacter getTargetableFilter() {
        return FilterCharacter.SELF;
    }

    @Override
    public EffectCharacter getTargetableEffect() {
        return Upgrade.effect;
    }
}
