package com.hearthsim.event.effect;

import com.hearthsim.card.Card;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class EffectHeroWeapon<T extends Card> implements EffectHero<T> {
    WeaponCard weapon;
    public EffectHeroWeapon(WeaponCard weapon) {
        this.weapon = weapon;
    }

    @Override
    public HearthTreeNode applyEffect(PlayerSide targetSide, HearthTreeNode boardState) {
        WeaponCard newWeapon = this.weapon;
        // if no origin is set then we have no idea whether we are in the original state. copy our base minion and summon a copy.
        // this is used for Minions with RNG battlecries (e.g. Bomb Lobber)
//        if (origin == null) {
//            newWeapon = weapon.deepCopy();
//        }
        newWeapon.hasBeenUsed(true);
        DeathrattleAction weaponDeathrattle = boardState.data_.getCurrentPlayer().getHero().setWeapon(newWeapon);
        if (weaponDeathrattle != null) {
            boardState = weaponDeathrattle.performAction(null, targetSide, boardState);
        }
        return boardState;
    }
}
