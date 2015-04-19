package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.MinionWithEnrage;

public class RagingWorgen extends MinionWithEnrage {

    public RagingWorgen() {
        super();
    }

    @Override
    public void enrage() {
        attack_ = (byte)(attack_ + 1);
        windFury_ = true;
    }

    @Override
    public void pacify() {
        attack_ = (byte)(attack_ - 1);
        windFury_ = false;
    }
}
