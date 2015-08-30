package com.hearthsim.card.goblinsvsgnomes.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class Cogmaster extends Minion {

    public Cogmaster() {
        super();
    }

    @Override
    public byte getTotalAttack(BoardModel boardModel, PlayerSide thisMinionPlayerSide) {
        byte origAttack = super.getTotalAttack(boardModel, thisMinionPlayerSide);
        if (boardModel.modelForSide(thisMinionPlayerSide).getNumMinions() <= 0)
            return origAttack;
        for (Minion minion : boardModel.modelForSide(thisMinionPlayerSide).getMinions()) {
            if (minion.getTribe() == MinionTribe.MECH) {
                return (byte)(origAttack + 2);
            }
        }
        return origAttack;
    }
}
