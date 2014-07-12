package com.hearthsim;

import java.io.IOException;
import java.nio.file.Path;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidCardException;
import com.hearthsim.io.DeckListFile;
import com.hearthsim.player.playercontroller.ArtificialPlayer;

/**
 * A Game setup to play a constructed deck vs constructed deck games.
 * 
 * The constructed decks must be specified in a DeckListFile.
 */
public class HearthSimConstructed extends HearthSim {

	Deck deck0_;
	Deck deck1_;
	
	HearthSimConstructed(Path deckListFilePath0, Path aiSetupFilePath0, Path deckListFilePath1, Path aiSetupFilePath1) throws IOException, HSInvalidCardException {
		deck0_ = (new DeckListFile(deckListFilePath0)).getDeck();
		deck1_ = (new DeckListFile(deckListFilePath1)).getDeck();
	}
	
	public GameResult runSingleGame() throws HSException {
	
		//------------------------------------------------------------------------		
		//Set up generic hero classes
		//------------------------------------------------------------------------
		Hero hero0 = new Hero();
		Hero hero1 = new Hero();
		
		//------------------------------------------------------------------------
		//Set up the AIs
		//------------------------------------------------------------------------
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
		
		return super.runSingleGame(ai0, hero0, deck0_, ai1, hero1, deck1_);
	}
}
