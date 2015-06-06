package com.hearthsim;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.results.GameResult;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HearthSimGUI extends HearthSimBase {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this
            .getClass());

    private Hero hero0_;
    private Hero hero1_;

    private Deck deck0_;
    private Deck deck1_;

    private ArtificialPlayer ai0_;
    private ArtificialPlayer ai1_;
    private ThreadPoolExecutor queue;

    /**
     * Constructor
     *
     * @param numSims
     * @param numThreads
     * @param hero0
     * @param deck0
     * @param ai0
     * @param hero1
     * @param deck1
     * @param ai1
     */
    public HearthSimGUI(int numSims, int numThreads, Hero hero0, Deck deck0,
            ArtificialPlayer ai0, Hero hero1, Deck deck1, ArtificialPlayer ai1) {
        super(numSims, numThreads);
        hero0_ = hero0;
        hero1_ = hero1;
        deck0_ = deck0;
        deck1_ = deck1;
        ai0_ = ai0;
        ai1_ = ai1;
    }

    @Override
    public void run() throws IOException, InterruptedException {
        log.info("running {} sims on {} threads", numSims_, numThreads_);
        long simStartTime = System.currentTimeMillis();

        // The cast is so we can see how many tasks complete.
        queue = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(this.numThreads_);
        for (int i = 0; i < numSims_; ++i) {
            GameThread gThread = new GameThread(i, null);
            queue.execute(gThread);
        }

        queue.shutdown();
        queue.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        long simEndTime = System.currentTimeMillis();
        double simDeltaTimeSeconds = (simEndTime - simStartTime) / 1000.0;
        String prettyDeltaTimeSeconds = String.format("%.2f",
                simDeltaTimeSeconds);
        double secondsPerGame = simDeltaTimeSeconds / numSims_;
        String prettySecondsPerGame = String.format("%.2f", secondsPerGame);

        log.info(
                "completed simulation of {} games in {} seconds on {} thread(s)",
                queue.getCompletedTaskCount(), prettyDeltaTimeSeconds,
                numThreads_);
        log.info("average time per game: {} seconds", prettySecondsPerGame);
    }

    @Override
    public GameResult runSingleGame(int gameId) throws IOException, HSException {

        return super.runSingleGame(ai0_.deepCopy(), hero0_.deepCopy(),
                deck0_.deepCopy(), ai1_.deepCopy(), hero1_.deepCopy(),
                deck1_.deepCopy(), gameId % 2);
    }

    public void stop() {
        queue.shutdownNow();
    }
}
