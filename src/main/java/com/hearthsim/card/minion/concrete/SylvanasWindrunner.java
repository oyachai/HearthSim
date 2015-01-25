package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleMindControl;

/**
 * Created by oyachai on 1/12/15.
 */
public class SylvanasWindrunner extends Minion {
    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public SylvanasWindrunner() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        deathrattleAction_ = new DeathrattleMindControl();
    }

}
