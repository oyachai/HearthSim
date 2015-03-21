package com.hearthsim.test

import com.hearthsim.Game
import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.ImplementedCardList
import com.hearthsim.card.minion.Minion
import com.hearthsim.card.minion.concrete.NoviceEngineer
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.card.spellcard.concrete.AnimalCompanion
import com.hearthsim.card.spellcard.concrete.ArcaneIntellect
import com.hearthsim.card.spellcard.concrete.Soulfire
import com.hearthsim.exception.HSException
import com.hearthsim.model.PlayerModel
import com.hearthsim.player.playercontroller.BruteForceSearchAI
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.util.HearthActionBoardPair

import spock.lang.Ignore

import static org.junit.Assert.*

import com.hearthsim.util.tree.HearthTreeNode;

class GameRepeatableSpec extends CardSpec {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    def "game history is repeatable"() {
        int numCardsInDeck_ = 12;
        byte minionAttack = 5;
        byte minionHealth = 4;
        byte minionMana = 4;

        Card[] cards1_ = new Card[numCardsInDeck_];
        Card[] cards2_ = new Card[numCardsInDeck_];

        for (int i = 0; i < numCardsInDeck_; ++i) {
            byte attack = minionAttack;
            byte health = minionHealth;
            byte mana = minionMana;
            cards1_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
            cards2_[i] = new Minion("" + i, mana, attack, health, attack, health, health);
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        def history = this.testRepeatableWithDecks(deck1, deck2);

        expect:
        // TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
        assertActionTreeIsRepeatable(history);
    }

    def "game history is repeatable with rng"() {
        int numCardsInDeck_ = 12;
        byte minionAttack = 5;
        byte minionHealth = 4;
        byte minionMana = 4;

        ArrayList<Card> cards1_ = new ArrayList<Card>();
        ArrayList<Card> cards2_ = new ArrayList<Card>();

        cards1_.add(new AnimalCompanion());
        cards2_.add(new AnimalCompanion());

        cards1_.add(new Soulfire());
        cards2_.add(new Soulfire());

        for (int i = cards1_.size(); i < numCardsInDeck_; ++i) {
            byte attack = minionAttack;
            byte health = minionHealth;
            byte mana = minionMana;
            cards1_.add(new Minion("" + i, mana, attack, health, attack, health, health));
            cards2_.add(new Minion("" + i, mana, attack, health, attack, health, health));
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        def history = this.testRepeatableWithDecks(deck1, deck2);

        expect:
        // TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
        assertActionTreeIsRepeatable(history);
    }

    def "game history is repeatable with card draw"() {
        int numCardsInDeck_ = 12;
        byte minionAttack = 5;
        byte minionHealth = 4;
        byte minionMana = 4;

        ArrayList<Card> cards1_ = new ArrayList<Card>();
        ArrayList<Card> cards2_ = new ArrayList<Card>();

        cards1_.add(new ArcaneIntellect());
        cards2_.add(new ArcaneIntellect());

        cards1_.add(new NoviceEngineer());
        cards2_.add(new NoviceEngineer());

        for (int i = cards1_.size(); i < numCardsInDeck_; ++i) {
            byte attack = minionAttack;
            byte health = minionHealth;
            byte mana = minionMana;
            cards1_.add(new Minion("" + i, mana, attack, health, attack, health, health));
            cards2_.add(new Minion("" + i, mana, attack, health, attack, health, health));
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        def history = this.testRepeatableWithDecks(deck1, deck2);

        expect:
        // TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
        assertActionTreeIsRepeatable(history);
    }

    @Ignore("Existing bug")
    def "with hogger"() {
        String[] names1 = ["Hogger","Hogger","Hogger","Hogger","Hogger","Hogger","Hogger"];
        String[] names2 = ["Hogger","Hogger","Hogger","Hogger","Hogger","Hogger","Hogger"];

        def history = this.testRepeatableWithCardNames(names1, names2);

        expect:
        // TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
        assertActionTreeIsRepeatable(history);
    }

    def "with stormpike commando"() {
        String[] names1 = ["Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando"];
        String[] names2 = ["Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando"];

        def history = this.testRepeatableWithCardNames(names1, names2);

        expect:
        // TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
        assertActionTreeIsRepeatable(history);
    }

    @Ignore("Throwaway")
    // This test is purely for trying to reproduce bugs exposed by the random test below
    def "testGameHistoryWithSpecificDecks"() {
        String[] names1 = ["Hungry Crab","Mana Addict","Scarlet Crusader","Boar","Harvest Golem","Amani Berserker","Arcane Golem","Mechanical Dragonling","Unleash the Hounds","Undertaker","Boar","Alexstrasza","Nerubian Egg","Master Swordsmith","Demonfire","Charge","Slam","Unleash the Hounds","Gnoll","Unbound Elemental","Arathi Weaponsmith","Heroic Strike","Arcane Shot","Faerie Dragon","Silence","Arcanite Reaper","Innervate","Big Game Hunter","Nat Pagle","Wicked Knife"]
        String[] names2 = ["Acolyte of Pain","Angry Chicken","Totemic Might","Northshire Cleric","Frostwolf Warlord","Spellbreaker","Circle of Healing","Stormpike Commando","Big Game Hunter","Jungle Panther","Stampeding Kodo","Shiv","Shadow Word: Death","Hellfire","Timber Wolf","Claw","Dalaran Mage","Gilblin Stalker","Hand of Protection","Force-Tank MAX","King Mukla","The Black Knight","Arathi Weaponsmith","Ancestral Spirit","Soot Spewer","Gorehowl","Raid Leader","Healing Touch","Mana Addict","Northshire Cleric"]

        def history = this.testRepeatableWithCardNames(names1, names2);

        expect:
        // TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
        assertActionTreeIsRepeatable(history);
    }

    @Ignore("Long and uses randomized data")
    // This test is purely for hunting new bugs. It can take a while to run and should not be included in the Travis test run
    def "testGameHistoryWithRandomDecks"() {
        int numCardsInDeck_ = 30;

        ImplementedCardList supportedCards = ImplementedCardList.getInstance();
        ArrayList<ImplementedCardList.ImplementedCard> allCards = supportedCards.getCardList();

        ArrayList<Card> cards1_ = new ArrayList<Card>();
        ArrayList<Card> cards2_ = new ArrayList<Card>();

        while(cards1_.size() < numCardsInDeck_) {
            int thisCardIndex = (int)Math.floor(Math.random() * (allCards.size() - 1));
            ImplementedCardList.ImplementedCard card = allCards.get(thisCardIndex);

            if (card.isHero) continue;

            cards1_.add(card.createCardInstance());
        }

        while(cards2_.size() < numCardsInDeck_) {
            int thisCardIndex = (int)Math.floor(Math.random() * (allCards.size() - 1));
            ImplementedCardList.ImplementedCard card = allCards.get(thisCardIndex);

            if (card.isHero) continue;

            cards2_.add(card.createCardInstance());
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        def history = this.testRepeatableWithDecks(deck1, deck2);

        expect:
        // TODO the output this generates is terrible and since the test is not repeatable it isn't too helpful right now
        assertActionTreeIsRepeatable(history);
    }

    def testRepeatableWithCardNames(String[] cardNames1, String[] cardNames2) throws HSException {
        ImplementedCardList supportedCards = ImplementedCardList.getInstance();

        ArrayList<Card> cards1_ = new ArrayList<Card>();
        ArrayList<Card> cards2_ = new ArrayList<Card>();

        for (String cardName : cardNames1) {
            ImplementedCardList.ImplementedCard card = supportedCards.getCardForName(cardName);

            if (card == null || card.isHero) {
                continue;
            }

            cards1_.add(card.createCardInstance());
        }

        for (String cardName : cardNames2) {
            ImplementedCardList.ImplementedCard card = supportedCards.getCardForName(cardName);

            if (card == null || card.isHero) {
                continue;
            }

            cards2_.add(card.createCardInstance());
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        return this.testRepeatableWithDecks(deck1, deck2);
    }

    def testRepeatableWithDecks(Deck deck1, Deck deck2) {
        log.info("deck 1\t" + deck1.toString());
        log.info("deck 2\t" + deck2.toString());

        def playerModel1 = new PlayerModel((byte)0, "player0", new TestHero(), deck1);
        def playerModel2 = new PlayerModel((byte)1, "player1", new TestHero(), deck2);

        def ai0 = BruteForceSearchAI.buildStandardAI1();
        def ai1 = BruteForceSearchAI.buildStandardAI1();

        def game = new Game(playerModel1, playerModel2, ai0, ai1, false);
        game.runGame();
        return game.gameHistory;

    }

    void assertActionTreeIsRepeatable(List<HearthActionBoardPair> history) {
        HearthTreeNode current = null;
        for (HearthActionBoardPair actionBoardPair in history) {
            if (current == null) {
                current = new HearthTreeNode(actionBoardPair.board.deepCopy());
            } else {
                assertNotNull(current);

                if (actionBoardPair.action == null) {
                    log.error("Node without action detected. Previous board state: " + current.data_.toJSON());
                    log.error("Node without action detected. Previous action: " + current.action.toJSON());
                    log.error("Node without action detected. Expected board state: " + actionBoardPair.board.toJSON());

                    assertBoardDelta(current.data_, actionBoardPair.board) {}
                    assertNotNull(actionBoardPair.action);
                }
                current = actionBoardPair.action.perform(current, false);

                assertNotNull("Should have new node after " + actionBoardPair.action.verb_ + " action", current);
                assertBoardDelta(actionBoardPair.board, current.data_) {
                }
            }
        }
    }
}
