package com.hearthsim.model;

import com.hearthsim.util.tree.HearthTreeNode;

/**
* Created by kallin on 2014-09-16.
*/ //todo: replace all the calls using 'getCurrentPlayer' and 'getWaitingPlayer' with one of these.
public enum PlayerSide {
    CURRENT_PLAYER,
    WAITING_PLAYER;

    public PlayerModel getPlayer(HearthTreeNode boardState) {
        return boardState.data_.modelForSide(this);
    }

    public PlayerModel getPlayer(BoardModel boardModel) {
        return boardModel.modelForSide(this);
    }
}
