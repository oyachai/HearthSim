package com.hearthsim.gui;

import com.hearthsim.event.HSGameEndEventListener;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.results.GameResult;
import org.apache.commons.math3.distribution.BetaDistribution;

import java.util.ArrayList;
import java.util.Observable;

public class HSMainFrameModel implements HSGameEndEventListener {

    private final HearthSim view_;
    private final HSSimulation simulation_;
    private boolean isRunning_;

    // Game result statistics
    final GameStats gameStats_;

    public class GameStats {
        ArrayList<GameResult> gameResults_;
        int p0_wins_;
        int p1_wins_;
        int[] p0_numMinionsOnTurn_;
        int[] p1_numMinionsOnTurn_;
        int[] p0_numCardsOnTurn_;
        int[] p1_numCardsOnTurn_;
        int[] p0_heroHealthOnTurn_;
        int[] p1_heroHealthOnTurn_;

        int p0_wins_fst_;
        int p0_fst_;

        int p1_wins_fst_;
        int p1_fst_;

        public GameStats() {
            gameResults_ = new ArrayList<>();
            p0_numMinionsOnTurn_ = new int[50];
            p1_numMinionsOnTurn_ = new int[50];
            p0_numCardsOnTurn_ = new int[50];
            p1_numCardsOnTurn_ = new int[50];
            p0_heroHealthOnTurn_ = new int[50];
            p1_heroHealthOnTurn_ = new int[50];
        }

        public synchronized void add(GameResult result) {
            gameResults_.add(result);
            if (result.winnerPlayerIndex_ == 0)
                ++p0_wins_;
            else if (result.winnerPlayerIndex_ == 1)
                ++p1_wins_;

            if (result.firstPlayerIndex_ == 0) {
                ++p0_fst_;
                if (result.winnerPlayerIndex_ == 0)
                    ++p0_wins_fst_;
            } else if (result.firstPlayerIndex_ == 1) {
                ++p1_fst_;
                if (result.winnerPlayerIndex_ == 1)
                    ++p1_wins_fst_;
            }

            int nR_0 = result.record_.getRecordLength(0);
            if (nR_0 > 50)
                nR_0 = 50;

            int nR_1 = result.record_.getRecordLength(1);
            if (nR_1 > 50)
                nR_1 = 50;

            for (int indx = 0; indx < nR_0; ++indx) {
                p0_numMinionsOnTurn_[indx] += result.record_.getNumMinions(0,
                        indx, 0);
            }
            for (int indx = 0; indx < nR_1; ++indx) {
                p1_numMinionsOnTurn_[indx] += result.record_.getNumMinions(1,
                        indx, 1);
            }
            for (int indx = 0; indx < nR_0; ++indx) {
                p0_numCardsOnTurn_[indx] += result.record_.getNumCardsInHand(0,
                        indx, 0);
            }
            for (int indx = 0; indx < nR_1; ++indx) {
                p1_numCardsOnTurn_[indx] += result.record_.getNumCardsInHand(1,
                        indx, 1);
            }
            for (int indx = 0; indx < nR_0; ++indx) {
                p0_heroHealthOnTurn_[indx] += result.record_.getHeroHealth(0,
                        indx, 0);
            }
            for (int indx = 0; indx < nR_1; ++indx) {
                p1_heroHealthOnTurn_[indx] += result.record_.getHeroHealth(1,
                        indx, 1);
            }
        }

        void reset() {
            p0_wins_ = 0;
            p1_wins_ = 0;
            p0_wins_fst_ = 0;
            p0_fst_ = 0;
            p1_wins_fst_ = 0;
            p1_fst_ = 0;
            gameResults_ = new ArrayList<>();
            p0_numMinionsOnTurn_ = new int[50];
            p1_numMinionsOnTurn_ = new int[50];
            p0_numCardsOnTurn_ = new int[50];
            p1_numCardsOnTurn_ = new int[50];
            p0_heroHealthOnTurn_ = new int[50];
            p1_heroHealthOnTurn_ = new int[50];
        }

        int getWins_p0() {
            return p0_wins_;
        }

        int getWins_p1() {
            return p1_wins_;
        }

        double getWinRate_p0() {
            if (p0_wins_ + p1_wins_ == 0)
                return 0.0;
            return p0_wins_ / ((double) p0_wins_ + (double) p1_wins_);
        }

        double getWinRate_p1() {
            if (p0_wins_ + p1_wins_ == 0)
                return 0.0;
            return p1_wins_ / ((double) p0_wins_ + (double) p1_wins_);
        }

        double getWinRateWhenGoingFirst_p0() {
            if (p0_fst_ == 0)
                return 0.0;
            return (double) p0_wins_fst_ / ((double) p0_fst_);
        }

        double getWinRateWhenGoingFirst_p1() {
            if (p1_fst_ == 0)
                return 0.0;
            return (double) p1_wins_fst_ / ((double) p1_fst_);
        }

        double getWinRateContRange_lower(double confPercent, int winCount,
                int totalNumGames) {
            BetaDistribution beta = new BetaDistribution(winCount,
                    totalNumGames - winCount + 1);
            return beta.inverseCumulativeProbability((1.0 - confPercent) * 0.5);
        }

        double getWinRateContRange_upper(double confPercent, int winCount,
                int totalNumGames) {
            BetaDistribution beta = new BetaDistribution(winCount + 1,
                    totalNumGames - winCount);
            return beta
                    .inverseCumulativeProbability(1.0 - (1.0 - confPercent) * 0.5);
        }

        double[] getAveNumMinions_p0() {
            int nGames = gameResults_.size();
            double[] toRet = new double[50];
            for (int indx = 0; indx < 50; ++indx) {
                toRet[indx] = (double) p0_numMinionsOnTurn_[indx]
                        / (double) nGames;
            }
            return toRet;
        }

        double[] getAveNumMinions_p1() {
            int nGames = gameResults_.size();
            double[] toRet = new double[50];
            for (int indx = 0; indx < 50; ++indx) {
                toRet[indx] = (double) p1_numMinionsOnTurn_[indx]
                        / (double) nGames;
            }
            return toRet;
        }

        double[] getAveNumCards_p0() {
            int nGames = gameResults_.size();
            double[] toRet = new double[50];
            for (int indx = 0; indx < 50; ++indx) {
                toRet[indx] = (double) p0_numCardsOnTurn_[indx]
                        / (double) nGames;
            }
            return toRet;
        }

        double[] getAveNumCards_p1() {
            int nGames = gameResults_.size();
            double[] toRet = new double[50];
            for (int indx = 0; indx < 50; ++indx) {
                toRet[indx] = (double) p1_numCardsOnTurn_[indx]
                        / (double) nGames;
            }
            return toRet;
        }

        double[] getAveHeroHealth_p0() {
            int nGames = gameResults_.size();
            double[] toRet = new double[50];
            for (int indx = 0; indx < 50; ++indx) {
                toRet[indx] = (double) p0_heroHealthOnTurn_[indx]
                        / (double) nGames;
            }
            return toRet;
        }

        double[] getAveHeroHealth_p1() {
            int nGames = gameResults_.size();
            double[] toRet = new double[50];
            for (int indx = 0; indx < 50; ++indx) {
                toRet[indx] = (double) p1_heroHealthOnTurn_[indx]
                        / (double) nGames;
            }
            return toRet;
        }
    }

    public HSMainFrameModel(HearthSim view) {
        view_ = view;
        simulation_ = new HSSimulation(this);
        gameStats_ = new GameStats();
        simulation_.getConfig().numSimulations_ = 10;
        simulation_.getConfig().numThreads_ = 1;
        simulation_.getConfig().simName_ = "HearthSim";

        simulation_.setAI_p0(BruteForceSearchAI.buildStandardAI2());
        simulation_.setAI_p1(BruteForceSearchAI.buildStandardAI2());
    }

    public HSSimulation getSimulation() {
        return simulation_;
    }

    public GameStats getGameStats() {
        return gameStats_;
    }

    @Override
    public synchronized void gameEnded(GameResult result) {
        gameStats_.add(result);
        view_.updatePlotPanel();
        view_.updateInfoPanel();
    }

    public boolean isRunning() {
        return this.isRunning_;
    }

    public void runSimulation() {
        simulation_.run();
        isRunning_ = true;
    }

    public void stopSimulation() {
        simulation_.stop();
        isRunning_ = false;
    }

    public void resetSimulationResults() {
        gameStats_.reset();
    }

    @Override
    public void update(Observable o, Object arg) {
        gameEnded((GameResult) arg);
    }
}
