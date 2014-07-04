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

public class HearthSim {
	
	int numSims_;
	
	int numCardsInDeck_;
	Card[] cards1_;
	Card[] cards2_;
	
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
	
	public int runSingleGame() throws HSException {
		cards1_ = new Card[numCardsInDeck_];
		cards2_ = new Card[numCardsInDeck_];

		for (int i = 0; i < numCardsInDeck_; ++i) {
			byte attack0 = (byte)((int)(Math.random() * maxMinionAttack_) + 1);
			byte health0 = (byte)((int)(Math.random() * maxMinionHealth_) + 1);
			byte mana0 = (byte)((int)(0.5 * (attack0 + health0)));
			cards1_[i] = new Minion("" + i, mana0, attack0, health0, attack0, health0, health0, false, false, false, false, false, false, true, false);

			byte attack1 = (byte)((int)(Math.random() * maxMinionAttack_) + 1);
			byte health1 = (byte)((int)(Math.random() * maxMinionHealth_) + 1);
			byte mana1 = (byte)((int)(0.5 * (attack1 + health1)));
			cards2_[i] = new Minion("" + i, mana1, attack1, health1, attack1, health1, health1, false, false, false, false, false, false, true, false);
		}
		
		int nt = 0;
		while (nt < numTaunts_) {
			int irand = (int)(Math.random() * numCardsInDeck_);
			if (!((Minion)cards1_[irand]).getTaunt()) {
				((Minion)cards1_[irand]).setTaunt(true);
				++nt;
			}
		}
		
		int ns = 0;
		while (ns < numHolySmite_) {
			int irand = (int)(Math.random() * numCardsInDeck_);
			if (!(cards1_[irand] instanceof SpellDamage)) {
				if (!((Minion)cards1_[irand]).getTaunt()) {
					cards1_[irand] = new SpellDamage("" + irand, (byte)1, (byte)2, false);
					++ns;
				}
			}
		}
		
		
		Hero hero1 = new Hero();
		Hero hero2 = new Hero();
		
		Deck deck1 = new Deck(cards1_);
		Deck deck2 = new Deck(cards2_);
		
		deck1.shuffle();
		deck2.shuffle();
		
		Player player1 = new Player("player0", hero1, deck1);
		Player player2 = new Player("player1", hero2, deck2);
		
		ArtificialPlayer ai0 = new ArtificialPlayer(
				p0_w_a_,
				p0_w_h_,
				p0_wt_a_,
				p0_wt_h_,
				p0_wTaunt_,
				p0_my_wHeroHealth_,
				p0_enemy_wHeroHealth_,
				p0_wMana_,
				p0_my_wNumMinions_,
				p0_enemy_wNumMinions_,
				p0_wSd_add_,
				p0_wSd_mult_
				);
		
		ArtificialPlayer ai1 = new ArtificialPlayer(
				p1_w_a_,
				p1_w_h_,
				p1_wt_a_,
				p1_wt_h_,
				p1_wTaunt_,
				p1_my_wHeroHealth_,
				p1_enemy_wHeroHealth_,
				p1_wMana_,
				p1_my_wNumMinions_,
				p1_enemy_wNumMinions_,
				p1_wSd_add_,
				p1_wSd_mult_
				);

		Game game = new Game(player1, player2, ai0, ai1);
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
				int winner = runSingleGame();
				if (winner == 0) {
					try {
						writer.write(0 + "\n");
						writer.flush();
					} catch (IOException e) { 
						System.err.println("Exception: " + e.getMessage());
						System.exit(1);
					}
					p0++;
				} else if (winner == 1) {
					try {
						writer.write(1 + "\n");
						writer.flush();
					} catch (IOException e) { 
						System.err.println("Exception: " + e.getMessage());
						System.exit(1);
					}
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
