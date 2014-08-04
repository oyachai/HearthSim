package com.hearthsim.gui;

import java.util.ArrayList;
import org.apache.commons.math3.distribution.*;

import com.hearthsim.GameResult;
import com.hearthsim.event.HSGameEndEventListener;
import com.hearthsim.util.BoardState;

public class HSMainFrameModel implements HSGameEndEventListener {

	private final HSMainFrame view_;
	private final HSSimulation simulation_;
	private boolean isRunning_;

	//Game result statistics
	final GameStats gameStats_;
	
	public class GameStats {
		ArrayList<GameResult> gameResults_;
		int p0_wins_;
		int p1_wins_;
		int[] p0_numMinionsOnTurn_;
		int[] p1_numMinionsOnTurn_;
		int[] p0_numCardsOnTurn_;
		int[] p1_numCardsOnTurn_;
		
		public GameStats() {
			gameResults_ = new ArrayList<GameResult>();
			p0_numMinionsOnTurn_ = new int[50];
			p1_numMinionsOnTurn_ = new int[50];
			p0_numCardsOnTurn_ = new int[50];
			p1_numCardsOnTurn_ = new int[50];
		}
		
		public synchronized void add(GameResult result) {
			gameResults_.add(result);
			if (result.winnerPlayerIndex_ == 0)
				++p0_wins_;
			else if (result.winnerPlayerIndex_ == 1)
				++p1_wins_;
			
			int nR_0 = result.record_.getRecordLength(0);
			if (nR_0 > 50) nR_0 = 50;
			
			int nR_1 = result.record_.getRecordLength(1);
			if (nR_1 > 50) nR_1 = 50;

			for (int indx = 0; indx < nR_0; ++indx) {
				BoardState board = result.record_.get(indx, 0);
				p0_numMinionsOnTurn_[indx] += board.getNumMinions_p0();
			}
			for (int indx = 0; indx < nR_1; ++indx) {
				BoardState board = result.record_.get(indx, 1);
				p1_numMinionsOnTurn_[indx] += board.getNumMinions_p0();
			}
			for (int indx = 0; indx < nR_0; ++indx) {
				BoardState board = result.record_.get(indx, 0);
				p0_numCardsOnTurn_[indx] += board.getNumCards_hand_p0();
			}
			for (int indx = 0; indx < nR_1; ++indx) {
				BoardState board = result.record_.get(indx, 1);
				p1_numCardsOnTurn_[indx] += board.getNumCards_hand_p0();
			}
		}
		
		void reset() {
			p0_wins_ = 0;
			p1_wins_ = 0;
			gameResults_ = new ArrayList<GameResult>();
			p0_numMinionsOnTurn_ = new int[50];
			p1_numMinionsOnTurn_ = new int[50];
			p0_numCardsOnTurn_ = new int[50];
			p1_numCardsOnTurn_ = new int[50];
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
			return (double)p0_wins_ / ((double)p0_wins_ + (double)p1_wins_);
		}

		double getWinRate_p1() {
			if (p0_wins_ + p1_wins_ == 0)
				return 0.0;
			return (double)p1_wins_ / ((double)p0_wins_ + (double)p1_wins_);
		}
		
		double getWinRateContRange_lower(double confPercent, int winCount, int totalNumGames) {
			BetaDistribution beta = new BetaDistribution(winCount, totalNumGames - winCount + 1);
			return beta.inverseCumulativeProbability((1.0 - confPercent) * 0.5);
		}

		double getWinRateContRange_upper(double confPercent, int winCount, int totalNumGames) {
			BetaDistribution beta = new BetaDistribution(winCount + 1, totalNumGames - winCount);
			return beta.inverseCumulativeProbability(1.0 - (1.0 - confPercent) * 0.5);
		}

		double[] getAveNumMinions_p0() {
			int nGames = gameResults_.size();
			double[] toRet = new double[50];
			for(int indx = 0; indx < 50; ++indx) {
				toRet[indx] = (double)p0_numMinionsOnTurn_[indx] / (double)nGames;
			}
			return toRet;
		}

		double[] getAveNumMinions_p1() {
			int nGames = gameResults_.size();
			double[] toRet = new double[50];
			for(int indx = 0; indx < 50; ++indx) {
				toRet[indx] = (double)p1_numMinionsOnTurn_[indx] / (double)nGames;
			}
			return toRet;
		}

		double[] getAveNumCards_p0() {
			int nGames = gameResults_.size();
			double[] toRet = new double[50];
			for(int indx = 0; indx < 50; ++indx) {
				toRet[indx] = (double)p0_numCardsOnTurn_[indx] / (double)nGames;
			}
			return toRet;
		}

		double[] getAveNumCards_p1() {
			int nGames = gameResults_.size();
			double[] toRet = new double[50];
			for(int indx = 0; indx < 50; ++indx) {
				toRet[indx] = (double)p1_numCardsOnTurn_[indx] / (double)nGames;
			}
			return toRet;
		}
	}
	
	public HSMainFrameModel(HSMainFrame view) {
		view_ = view;
		simulation_ = new HSSimulation(this);
		gameStats_ = new GameStats();
	}
	
	public HSSimulation getSimulation() {
		return simulation_;
	}

	public GameStats getGameStats() {
		return gameStats_;
	}
	
	@Override
	public void gameEnded(GameResult result) {
		// TODO Auto-generated method stub
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
		isRunning_ = false;
	}
	
	public void resetSimulationResults() {
		gameStats_.reset();
	}
}
