package com.hearthsim;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSParamNotFoundException;
import com.hearthsim.io.ParamFile;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.util.ThreadQueue;
import com.json.JSONObject;

public abstract class HearthSim {
	
	int numSims_;
	int numThreads_;
	String gameResultFileName_;
	protected Path rootPath_;
	
	protected Path aiParamFilePath0_;
	protected Path aiParamFilePath1_;
	
	
	GameResult[] results_;
	/**
	 * Constructor
	 * 
	 * @param setupFilePath
	 * @throws HSInvalidParamFileException
	 * @throws HSParamNotFoundException
	 * @throws IOException
	 */
	HearthSim(Path setupFilePath) throws HSInvalidParamFileException, HSParamNotFoundException, IOException {
		rootPath_ = setupFilePath.getParent();
		ParamFile masterParam = new ParamFile(setupFilePath);
		numSims_ = masterParam.getInt("num_simulations", 40000);
		numThreads_ = masterParam.getInt("num_threads", 1);
		aiParamFilePath0_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("aiParamFilePath0"));
		aiParamFilePath1_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("aiParamFilePath1"));
		gameResultFileName_ = masterParam.getString("output_file", "gameres.txt");
		results_ = new GameResult[numSims_];
	}
		
	/**
	 * Run a single game.
	 * 
	 * Must be overridden by a concrete subclass.  The subclass's job is to set up the decks and the AIs and to 
	 * call runSigleGame(ArtificialPlayer, Hero, Deck, ArtificialPlayer, Hero, Deck).
	 * 
	 * @return
	 * @throws HSException
	 * @throws IOException 
	 */
	public abstract GameResult runSingleGame() throws HSException, IOException;
	
	/**
	 * Run a single game
	 * 
	 * @param ai0 AI for player 0
	 * @param hero0 Hero class for player 0
	 * @param deck0 Deck for player 0
	 * @param ai1 AI for player 1
	 * @param hero1 Hero class for player 1
	 * @param deck1 Deck for player 1
	 * @return
	 * @throws HSException
	 */
	protected GameResult runSingleGame(ArtificialPlayer ai0, Hero hero0, Deck deck0, ArtificialPlayer ai1, Hero hero1, Deck deck1) throws HSException {

		//Shuffle the decks!
		deck0.shuffle();
		deck1.shuffle();
		
		Player player0 = new Player("player0", new Hero(), deck0);
		Player player1 = new Player("player1", new Hero(), deck1);

		Game game = new Game(player0, player1, ai0, ai1);
		return game.runGame();
	}
	
	
	public void run() throws HSException, IOException, InterruptedException {
		Path outputFilePath = FileSystems.getDefault().getPath(rootPath_.toString(), gameResultFileName_);
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath.toString()), "utf-8"));

		ThreadQueue tQueue = new ThreadQueue(numThreads_);
		for (int i = 0; i < numSims_; ++i) {
			GameThread gThread = new GameThread(i, writer);
			tQueue.queue(gThread);
		}

		tQueue.runQueue();
		writer.close();

		System.out.println("done");
	}
	
	
	private class GameThread implements Runnable {

		
		final int gameId_;
		Writer writer_;
		
		public GameThread(int gameId, Writer writer) {
			gameId_ = gameId;
			writer_ = writer;
		}
		
		@Override
		public void run() {
			try {
				GameResult res = runSingleGame();
				synchronized(writer_) {
					System.out.println("game " + gameId_ + ", player " + res.winnerPlayerIndex_ + " wins");
					GameResultSummary grs = new GameResultSummary(res);
					writer_.write(grs.toJSON().toString() + "\n");
					writer_.flush();
					results_[gameId_] = res;
				}
			} catch (HSException | IOException e) {
				System.out.println("Error! " + e);
				e.printStackTrace();
			}
		}
		
	}
}
