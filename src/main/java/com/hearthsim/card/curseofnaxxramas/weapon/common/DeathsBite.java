package com.hearthsim.card.curseofnaxxramas.weapon.common;

import com.hearthsim.card.weapon.WeaponCard;
import com.hearthsim.event.deathrattle.DeathrattleDamageAllMinions;

public class DeathsBite extends WeaponCard {
    public DeathsBite() {
        super();
        this.deathrattleAction_ = new DeathrattleDamageAllMinions((byte) 1);
    }
}
