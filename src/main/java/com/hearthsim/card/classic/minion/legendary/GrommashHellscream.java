package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.minion.MinionWithEnrage;

public class GrommashHellscream extends MinionWithEnrage {

    public GrommashHellscream() {
        super();
    }

    @Override
    public void enrage() {
        attack_ = (byte)(attack_ + 6);
    }

    @Override
    public void pacify() {
        attack_ = (byte)(attack_ - 6);
    }
}
