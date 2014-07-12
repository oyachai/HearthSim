package com.hearthsim;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellDamage;
import com.hearthsim.exception.HSException;
import com.hearthsim.player.Player;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.json.*;

public abstract class HearthSim {
	
	int numSims_;
	
	int numCardsInDeck_;
	
	int maxMinionAttack_;
	int maxMinionHealth_;

	int numTaunts_;
	
	//for holy smite studies
	int numHolySmite_;
	int holySmiteDamage_;
	int holySmiteMana_;
	
	double p0_w_a_;
	double p0_w_h_;
	double p0_wt_a_;
	double p0_wt_h_;
	double p0_wTaunt_;
	double p0_my_wHeroHealth_;
	double p0_enemy_wHeroHealth_;
	double p0_wMana_;
	double p0_wSd_mult_;
	double p0_wSd_add_;
	double p0_my_wNumMinions_;
	double p0_enemy_wNumMinions_;
	
	double p1_w_a_;
	double p1_w_h_;
	double p1_wt_a_;
	double p1_wt_h_;
	double p1_wTaunt_;
	double p1_my_wHeroHealth_;
	double p1_enemy_wHeroHealth_;
	double p1_wMana_;
	double p1_wSd_mult_;
	double p1_wSd_add_;
	double p1_my_wNumMinions_;
	double p1_enemy_wNumMinions_;
	
	String gameResultFileName_;
	
	public void setup(Path setupFilePath) throws IOException {
		String inStr = new String(Files.readAllBytes(setupFilePath));
		JSONObject inJson = new JSONObject(inStr);
		this.setup(inJson);
	}
	
	public void setup(JSONObject inJson) {
				
		numSims_ = inJson.optInt("num_simulations", 40000);
		
		numCardsInDeck_ = inJson.optInt("num_cards_in_deck", 30);
		maxMinionAttack_ = inJson.optInt("max_minion_attack", 4);
		maxMinionHealth_ = inJson.optInt("max_minion_health", 3);
		
		numTaunts_ = inJson.optInt("num_taunts", 0);
		numHolySmite_ = inJson.optInt("num_holy_smite", 0);
		holySmiteDamage_ = inJson.optInt("holy_smite_damage", 2);
		holySmiteMana_ = inJson.optInt("holy_smite_mana", 1);
		
		p0_w_a_ = inJson.optDouble("p0_w_a", 0.9);
		p0_w_h_ = inJson.optDouble("p0_w_h", 0.9);
		p0_wt_a_ = inJson.optDouble("p0_wt_a", 1.0);
		p0_wt_h_ = inJson.optDouble("p0_wt_h", 1.0);
		p0_wTaunt_ = inJson.optDouble("p0_w_taunt", 0.0);
		p0_my_wHeroHealth_ = inJson.optDouble("p0_my_w_hero_health", 0.1);
		p0_enemy_wHeroHealth_ = inJson.optDouble("p0_enemy_w_hero_health", 0.1);
		p0_wMana_ = inJson.optDouble("p0_w_mana", 0.1);
		p0_wSd_mult_ = inJson.optDouble("p0_w_sd_mult", 1.0);
		p0_wSd_add_ = inJson.optDouble("p0_w_sd_add", 0.9);
		p0_my_wNumMinions_ = inJson.optDouble("p0_my_w_num_minions", 0.5);
		p0_enemy_wNumMinions_ = inJson.optDouble("p0_enemy_w_num_minions", 0.5);
		
		p1_w_a_ = inJson.optDouble("p1_w_a", 0.9);
		p1_w_h_ = inJson.optDouble("p1_w_h", 0.9);
		p1_wt_a_ = inJson.optDouble("p1_wt_a", 1.0);
		p1_wt_h_ = inJson.optDouble("p1_wt_h", 1.0);
		p1_wTaunt_ = inJson.optDouble("p1_w_taunt", 0.0);
		p1_my_wHeroHealth_ = inJson.optDouble("p1_my_w_hero_health", 0.1);
		p1_enemy_wHeroHealth_ = inJson.optDouble("p1_enemy_w_hero_health", 0.1);
		p1_wMana_ = inJson.optDouble("p1_w_mana", 0.1);
		p1_wSd_mult_ = inJson.optDouble("p1_w_sd_mult", 1.0);
		p1_wSd_add_ = inJson.optDouble("p1_w_sd_add", 0.9);
		p1_my_wNumMinions_ = inJson.optDouble("p1_my_w_num_minions", 0.5);
		p1_enemy_wNumMinions_ = inJson.optDouble("p1_enemy_w_num_minions", 0.5);
		
		gameResultFileName_ = inJson.optString("output_file", "gameres.txt");
	}
	
	/**
	 * Run a single game.
	 * 
	 * Must be overridden by a concrete subclass.  The subclass's job is to set up the decks and the AIs and to 
	 * call runSigleGame(ArtificialPlayer, Hero, Deck, ArtificialPlayer, Hero, Deck).
	 * 
	 * @return
	 * @throws HSException
	 */
	public abstract GameResult runSingleGame() throws HSException;
	
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
	
	
	public void run() {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameResultFileName_), "utf-8"));
		} catch (IOException e) { 
			System.err.println("Exception: " + e.getMessage());
		}
		int p0 = 0;
		int p1 = 0;
		for (int i = 0; i < numSims_; ++i) {
			try {
				GameResult res = runSingleGame();
				try {
					writer.write(res.winnerPlayerIndex_ + "," + res.gameDuration_ + "\n");
					writer.flush();
				} catch (IOException e) { 
					System.err.println("Exception: " + e.getMessage());
					System.exit(1);
				}
				if (res.winnerPlayerIndex_ == 0) {
					p0++;
				} else if (res.winnerPlayerIndex_ == 1) {
					p1++;
				} else {
					
				}
			} catch (HSException e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}
		
		try {
			writer.close();
		} catch (IOException e) { 
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}
		System.out.println("w0 = " + p0 + ", w1 = " + p1);
		System.out.println("p0 = " + (p0 / (double)numSims_) + ", p1 = " + (p1 / (double)numSims_));
		
		System.out.println("done");
	}
}
