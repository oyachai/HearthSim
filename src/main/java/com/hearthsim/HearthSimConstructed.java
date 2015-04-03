package com.hearthsim;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSException;
import com.hearthsim.exception.HSInvalidParamFileException;
import com.hearthsim.exception.HSParamNotFoundException;
import com.hearthsim.io.DeckListFile;
import com.hearthsim.io.ParamFile;
import com.hearthsim.player.playercontroller.ArtificialPlayer;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.results.GameResult;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * A Game setup to play a constructed deck vs constructed deck games.
 *
 * The constructed decks must be specified in a DeckListFile.
 */
public class HearthSimConstructed extends HearthSimBase {

    private Path deckListFilePath0_;
    private Path deckListFilePath1_;

    HearthSimConstructed(Path setupFilePath) throws IOException, HSInvalidParamFileException, HSParamNotFoundException {
        super(setupFilePath);
        ParamFile masterParam = new ParamFile(setupFilePath);
        deckListFilePath0_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("deckListFilePath0"));
        deckListFilePath1_ = FileSystems.getDefault().getPath(rootPath_.toString(), masterParam.getString("deckListFilePath1"));
    }

    @Override
    public GameResult runSingleGame(int gameId) throws IOException, HSException {

        DeckListFile deckList0 = new DeckListFile(deckListFilePath0_);
        DeckListFile deckList1 = new DeckListFile(deckListFilePath1_);

        Hero hero0 = deckList0.getHero();
        Hero hero1 = deckList1.getHero();

        Deck deck0 = deckList0.getDeck();
        Deck deck1 = deckList1.getDeck();

        ArtificialPlayer ai0 = new BruteForceSearchAI(this.aiParamFilePath0_);
        ArtificialPlayer ai1 = new BruteForceSearchAI(this.aiParamFilePath1_);

        return super.runSingleGame(ai0, hero0, deck0, ai1, hero1, deck1, gameId % 2);
    }
}
