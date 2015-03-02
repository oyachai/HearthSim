package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.MinionWithEnrage;

public class AmaniBerserker extends MinionWithEnrage {

    private static final boolean HERO_TARGETABLE = true;

    public AmaniBerserker() {
        super();
        heroTargetable_ = HERO_TARGETABLE;
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
