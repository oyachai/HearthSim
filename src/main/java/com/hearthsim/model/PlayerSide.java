package com.hearthsim.model;

import com.hearthsim.util.tree.HearthTreeNode;

/**
* Created by kallin on 2014-09-16.
*/ //TODO: replace all the calls using 'getCurrentPlayer' and 'getWaitingPlayer' with one of these.
public enum PlayerSide {
    CURRENT_PLAYER,
    WAITING_PLAYER;

    @Deprecated
    public PlayerModel getPlayer(HearthTreeNode boardState) {
        return this.getPlayer(boardState.data_);
    }

    @Deprecated
    protected PlayerModel getPlayer(BoardModel boardModel) {
        return boardModel.modelForSide(this);
    }

    public PlayerSide getOtherPlayer() {
        if (this == CURRENT_PLAYER) {
            return WAITING_PLAYER;
        } else {
            return CURRENT_PLAYER;
        }
    }
}
