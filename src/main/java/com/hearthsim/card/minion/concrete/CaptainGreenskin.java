package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.effect.CardEffectCharacter;

public class CaptainGreenskin extends Minion implements MinionBattlecryInterface {

    public CaptainGreenskin() {
        super();
    }

    /**
     * Battlecry: Add +1/+1 to your weapon.
     */
    @Override
    public CardEffectCharacter getBattlecryEffect() {
        return (originSide, origin, targetSide, targetCharacterIndex, boardState) -> {
            WeaponCard weapon = boardState.data_.getCurrentPlayer().getHero().getWeapon();
            if (weapon != null) {
                weapon.setWeaponDamage((byte) (weapon.getWeaponDamage() + 1));
                weapon.setWeaponCharge((byte)(weapon.getWeaponCharge() + 1));
            }
            return boardState;
        };
    }
}
