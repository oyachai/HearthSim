package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.MinionWithEnrage;

public class TaurenWarrior extends MinionWithEnrage {

    public TaurenWarrior() {
        super();
    }

    @Override
    public void enrage() {
        attack_ = (byte)(attack_ + 3);
    }

    @Override
    public void pacify() {
        attack_ = (byte)(attack_ - 3);
    }

}
