package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectHeroWeaponBuffDelta<T extends Card> implements EffectHero<T> {
    private byte attackDelta;

    private byte durabilityDelta;

    public EffectHeroWeaponBuffDelta(int attackDelta) {
        this(attackDelta, 0);
    }

    public EffectHeroWeaponBuffDelta(int attackDelta, int durabilityDelta) {
        this.attackDelta = (byte) attackDelta;
        this.durabilityDelta = (byte) durabilityDelta;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, HearthTreeNode boardState) {
        Hero hero = (Hero)boardState.data_.getCharacter(targetSide, CharacterIndex.HERO);
        WeaponCard weapon = hero.getWeapon();
        if (weapon != null) {
            weapon.addWeaponDamage(this.attackDelta);
            weapon.addWeaponCharge(this.durabilityDelta);
        }
        return boardState;
    }
}
