package com.hearthsim.event;

import java.util.Observer;

import com.hearthsim.results.GameResult;

public interface HSGameEndEventListener extends Observer {

    public void gameEnded(GameResult result);

}
