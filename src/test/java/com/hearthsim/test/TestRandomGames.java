package com.hearthsim.test;

import com.hearthsim.Game;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.heroes.TestHero;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.DeckFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by oyachai on 6/11/15.
 */
public class TestRandomGames {

    @Ignore
    @Test
    public void testTargetOwnHero() throws HSException {

        double minutesToRun = 10.0;

        Hero hero = new TestHero();
        DeckFactory factory = new DeckFactory.DeckFactoryBuilder().buildDeckFactory();

        int result = 0;
        long start = System.currentTimeMillis();
        BruteForceSearchAI ai = BruteForceSearchAI.buildStandardAI2();
        while((System.currentTimeMillis() - start) / 60000.0 < minutesToRun) {
            PlayerModel model0 = new PlayerModel((byte) 0, "", hero.deepCopy(), factory.generateRandomDeck());
            PlayerModel model1 = new PlayerModel((byte) 1, "", hero.deepCopy(), factory.generateRandomDeck());
            Game testGame = new Game(model0, model1, ai.deepCopy(), ai.deepCopy());
            testGame.runGame();
            result++;
        }
    }

}
