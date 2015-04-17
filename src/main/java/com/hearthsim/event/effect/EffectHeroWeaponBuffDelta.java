package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectHeroWeaponBuffDelta<T extends Card> implements EffectHero<T> {
    private byte attackDelta;

    public EffectHeroWeaponBuffDelta(int attackDelta) {
        this.attackDelta = (byte) attackDelta;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide originSide, Card origin, PlayerSide targetSide, HearthTreeNode boardState) {
        Hero hero = (Hero)boardState.data_.getCharacter(targetSide, 0);
        WeaponCard weapon = hero.getWeapon();
        if (weapon != null) {
            weapon.addWeaponDamage(this.attackDelta);
        }
        return boardState;
    }
}
