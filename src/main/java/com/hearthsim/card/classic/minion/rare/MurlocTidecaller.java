package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MurlocTidecaller extends Minion implements MinionSummonedInterface {

    public MurlocTidecaller() {
        super();
    }

    @Override
    public HearthTreeNode minionSummonEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState) {
        if (summonedMinion != this && summonedMinion.getTribe() == MinionTribe.MURLOC) {
            this.addAttack((byte) 1);
        }
        return boardState;
    }
}
