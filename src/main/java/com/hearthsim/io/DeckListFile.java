package com.hearthsim.io;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSInvalidCardException;
import com.hearthsim.exception.HSInvalidHeroException;
import com.hearthsim.util.CardFactory;
import com.hearthsim.util.HeroFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class to read/write a HearthSim deck specification file
 *
 */
public class DeckListFile {

    private Deck deck_;
    private Hero hero_;

    /**
     * Constructor
     *
     * @param setupFilePath
     *            The file path to the deck list to be read
     * @throws IOException
     * @throws HSInvalidHeroException
     */
    public DeckListFile(Path setupFilePath) throws HSInvalidCardException,
            IOException, HSInvalidHeroException {
        this.read(setupFilePath);
    }

    /**
     * Constructor
     *
     * @param heroName - name of the class, e.g. Hunter
     * @param deck
     * @throws IOException
     * @throws HSInvalidHeroException
     */
    public DeckListFile(String heroName, Deck deck) throws IOException,
            HSInvalidHeroException {
        deck_ = deck;
        hero_ = HeroFactory.getHero(heroName);
    }

    /**
     * Read the given file
     *
     * @param setupFilePath
     *            The file path to the deck list to be read
     * @throws IOException
     * @throws HSInvalidHeroException
     */
    protected void read(Path setupFilePath) throws HSInvalidCardException,
            IOException, HSInvalidHeroException {
        String inStr = new String(Files.readAllBytes(setupFilePath))
            .replace("\\s+", "").replace("'", "").replace("\n", "");
        this.parseDeckList(inStr);
    }

    /**
     * Parse a given deck list string and construct a Deck object out of it
     *
     * @param deckListStr
     */
    protected void parseDeckList(String deckListStr)
            throws HSInvalidCardException, HSInvalidHeroException {
        String[] deckList = deckListStr.split(",");
        ArrayList<Card> cards = new ArrayList<>();
        // Ignore the first entry for now... hero classes aren't implemented
        // yet!
        for (int indx = 1; indx < deckList.length; ++indx) {
            cards.add(CardFactory.getCard(deckList[indx]));
        }
        deck_ = new Deck(cards);
        hero_ = HeroFactory.getHero(deckList[0]);
    }

    /**
     * Serialize a deck to a .hsdeck file
     *
     * @param saveFile
     *            - File with path and file name. The .hsdeck is automatically
     *            appended and should not be included as part of the file name.
     * @param heroName
     * @param deck
     * @throws IOException
     */
    public void writeDeckListToFile(File saveFile) throws IOException {
        // add .hsdeck extension if missing
        if (!saveFile.toString().endsWith(".hsdeck")) {
            saveFile = new File(saveFile + ".hsdeck");
        }

        FileWriter fw = new FileWriter(saveFile);
        fw.write(hero_.getHeroClass() + "," + System.getProperty("line.separator"));
        fw.write(deck_.getDeckList());
        fw.close();
    }

    /**
     * Get the deck
     *
     * @return
     */
    public Deck getDeck() {
        return deck_;
    }

    /**
     * Get the Hero
     *
     * @return
     */
    public Hero getHero() {
        return hero_;
    }
}
