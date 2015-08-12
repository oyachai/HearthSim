package com.hearthsim.event.deathrattle;

import com.hearthsim.card.CharacterIndex;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleDamageAllMinions extends DeathrattleAction {

    private final byte damage_;

    public DeathrattleDamageAllMinions(byte damage) {
        damage_ = damage;
    }

    @Override
    public HearthTreeNode performAction(CharacterIndex originIndex,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState) {
        HearthTreeNode toRet = super.performAction(originIndex, playerSide, boardState);
        if (toRet != null) {
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            PlayerModel waitingPlayer = toRet.data_.modelForSide(PlayerSide.WAITING_PLAYER);
            for (Minion aMinion : waitingPlayer.getMinions()) {
                toRet = aMinion.takeDamageAndNotify(damage_, playerSide, PlayerSide.WAITING_PLAYER, toRet, false, false);
            }
            for (Minion aMinion : currentPlayer.getMinions()) {
                toRet = aMinion.takeDamageAndNotify(damage_, playerSide, PlayerSide.CURRENT_PLAYER, toRet, false, false);
            }
        }
        return toRet;
    }
}
