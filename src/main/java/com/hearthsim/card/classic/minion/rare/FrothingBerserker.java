package com.hearthsim.card.classic.minion.rare;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionDamagedInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FrothingBerserker extends Minion implements MinionDamagedInterface {

    public FrothingBerserker() {
        super();
    }

    /**
     * Whenever a minion takes damage, gain 1 attack
     * */
    public HearthTreeNode minionDamagedEvent(PlayerSide thisMinionPlayerSide, PlayerSide damagedPlayerSide, Minion damagedMinion, HearthTreeNode boardState) {
        this.addAttack((byte)1);
        return boardState;
    }
}
