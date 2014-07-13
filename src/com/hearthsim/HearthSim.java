package com.hearthsim;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSParamNotFoundException;
import com.hearthsim.io.ParamFile;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.json.*;

public abstract class HearthSim {
	
	int numSims_;
	String gameResultFileName_;
	protected Path rootPath_;
	
	protected Path aiParamFilePath0_;
	protected Path aiParamFilePath1_;
	
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
		aiParamFilePath0_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("aiParamFilePath0"));
		aiParamFilePath1_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("aiParamFilePath1"));
		gameResultFileName_ = masterParam.getString("output_file", "gameres.txt");
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
	
	
	public void run() throws HSException, IOException {
		Path outputFilePath = FileSystems.getDefault().getPath(rootPath_.toString(), gameResultFileName_);
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath.toString()), "utf-8"));
		int p0 = 0;
		int p1 = 0;
		for (int i = 0; i < numSims_; ++i) {
			GameResult res = runSingleGame();
			writer.write(res.winnerPlayerIndex_ + "," + res.gameDuration_ + "\n");
			writer.flush();
			if (res.winnerPlayerIndex_ == 0) {
				p0++;
			} else if (res.winnerPlayerIndex_ == 1) {
				p1++;
			} else {
				
			}
		}

		writer.close();
		
		System.out.println("w0 = " + p0 + ", w1 = " + p1);
		System.out.println("p0 = " + (p0 / (double)numSims_) + ", p1 = " + (p1 / (double)numSims_));
		
		System.out.println("done");
	}
}
