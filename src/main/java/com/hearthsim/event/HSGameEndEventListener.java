package com.hearthsim.event;

import com.hearthsim.results.GameResult;

import java.util.Observer;

public interface HSGameEndEventListener extends Observer {

    public void gameEnded(GameResult result);

}
