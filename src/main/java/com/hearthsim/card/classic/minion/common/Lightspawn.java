package com.hearthsim.card.classic.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Lightspawn extends Minion {

    public Lightspawn() {
        super();
    }

    @Override
    public byte getTotalAttack(BoardModel boardModel, PlayerSide thisMinionPlayerSide) {
        return this.getTotalHealth();
    }
}
