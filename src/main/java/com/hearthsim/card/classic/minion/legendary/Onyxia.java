package com.hearthsim.card.classic.minion.legendary;

import com.hearthsim.card.classic.minion.common.Whelp;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.EffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Onyxia extends Minion implements MinionBattlecryInterface {

    public Onyxia() {
        super();
    }

    @Override
    public EffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            HearthTreeNode toRet = boardState;
            PlayerModel currentPlayer = toRet.data_.getCurrentPlayer();
            while (!currentPlayer.isBoardFull()) {
                Minion placementTarget;
                if (currentPlayer.getNumMinions() % 2 == 0) {
                    placementTarget = toRet.data_.getCurrentPlayer().getCharacter(currentPlayer.getIndexForCharacter(this) - 1);
                } else {
                    placementTarget = this;
                }
                toRet = new Whelp().summonMinion(PlayerSide.CURRENT_PLAYER, placementTarget, toRet, false, false);
            }
            return toRet;
        };
    }
}
