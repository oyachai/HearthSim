package com.hearthsim.util;

import com.hearthsim.model.BoardModel;

public class HearthActionBoardPair {

    public final HearthAction action;
    public final BoardModel board;

    public HearthActionBoardPair(HearthAction action, BoardModel board) {
        this.action = action;
        this.board = board;
    }

}
