package com.hearthsim;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidCardException;
import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSParamNotFoundException;
import com.hearthsim.io.DeckListFile;
import com.hearthsim.io.ParamFile;
import com.hearthsim.player.playercontroller.ArtificialPlayer;

/**
 * A Game setup to play a constructed deck vs constructed deck games.
 * 
 * The constructed decks must be specified in a DeckListFile.
 */
public class HearthSimConstructed extends HearthSimBase {

	Path deckListFilePath0_;
	Path deckListFilePath1_;
		
	HearthSimConstructed(Path setupFilePath) throws IOException, HSInvalidCardException, HSInvalidParamFileException, HSParamNotFoundException {
		super(setupFilePath);
		ParamFile masterParam = new ParamFile(setupFilePath);
		deckListFilePath0_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("deckListFilePath0"));
		deckListFilePath1_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("deckListFilePath1"));
	}
	
	@Override
	public GameResult runSingleGame() throws IOException, HSException {
	
		DeckListFile deckList0 = new DeckListFile(deckListFilePath0_);
		DeckListFile deckList1 = new DeckListFile(deckListFilePath1_);

		//------------------------------------------------------------------------		
		//Set up generic hero classes
		//------------------------------------------------------------------------
		Hero hero0 = deckList0.getHero();
		Hero hero1 = deckList1.getHero();

		//------------------------------------------------------------------------		
		//Set up generic hero classes
		//------------------------------------------------------------------------
		Deck deck0 = deckList0.getDeck();
		Deck deck1 = deckList1.getDeck();
		
		//------------------------------------------------------------------------
		//Set up the AIs
		//------------------------------------------------------------------------
		ArtificialPlayer ai0 = new ArtificialPlayer(this.aiParamFilePath0_);
		ArtificialPlayer ai1 = new ArtificialPlayer(this.aiParamFilePath1_);
		
		return super.runSingleGame(ai0, hero0, deck0, ai1, hero1, deck1);
	}
}
