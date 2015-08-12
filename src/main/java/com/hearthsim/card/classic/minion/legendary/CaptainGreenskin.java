package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.effect.EffectCharacter;

public class CaptainGreenskin extends Minion implements MinionBattlecryInterface {

    public CaptainGreenskin() {
        super();
    }

    /**
     * Battlecry: Add +1/+1 to your weapon.
     */
    @Override
    public EffectCharacter getBattlecryEffect() {
        return (targetSide, targetCharacterIndex, boardState) -> {
            WeaponCard weapon = boardState.data_.getCurrentPlayer().getHero().getWeapon();
            if (weapon != null) {
                weapon.setWeaponDamage((byte) (weapon.getWeaponDamage() + 1));
                weapon.setWeaponCharge((byte)(weapon.getWeaponCharge() + 1));
            }
            return boardState;
        };
    }
}
