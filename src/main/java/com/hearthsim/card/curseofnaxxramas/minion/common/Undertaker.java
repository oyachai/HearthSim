package com.hearthsim.card.curseofnaxxramas.minion.common;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionSummonedInterface;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Undertaker extends Minion implements MinionSummonedInterface {

    public Undertaker() {
        super();
    }

    /**
     * Whenever a minion with Deathrattle is summoned, gain +1 Attack
     * */
    @Override
    public HearthTreeNode minionSummonEvent(
            PlayerSide thisMinionPlayerSide,
            PlayerSide summonedMinionPlayerSide,
            Minion summonedMinion,
            HearthTreeNode boardState) {
        if (boardState != null && summonedMinion.hasDeathrattle() && thisMinionPlayerSide == summonedMinionPlayerSide) {
            this.addAttack((byte)1);
        }
        return boardState;
    }
}
