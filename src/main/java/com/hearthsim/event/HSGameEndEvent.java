package com.hearthsim.event;

import com.hearthsim.results.GameResult;

public class HSGameEndEvent {

    private final GameResult result_;

    HSGameEndEvent(GameResult result) {
        result_ = result;
    }

    GameResult getResult() {
        return result_;
    }
}
