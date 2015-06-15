package com.hearthsim.test

import com.hearthsim.Game
import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.ImplementedCardList
import com.hearthsim.card.basic.minion.NoviceEngineer
import com.hearthsim.card.basic.spell.AnimalCompanion
import com.hearthsim.card.basic.spell.ArcaneIntellect
import com.hearthsim.card.basic.spell.Soulfire
import com.hearthsim.card.minion.MinionMock
import com.hearthsim.card.minion.heroes.TestHero
import com.hearthsim.exception.HSException
import com.hearthsim.model.PlayerModel
import com.hearthsim.player.playercontroller.BruteForceSearchAI
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.util.HearthActionBoardPair
import com.hearthsim.util.tree.HearthTreeNode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Ignore

import static org.junit.Assert.assertNotNull;

class GameRepeatableSpec extends CardSpec {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
            cards1_[i] = new MinionMock("" + i, mana, attack, health, attack, health, health);
            cards2_[i] = new MinionMock("" + i, mana, attack, health, attack, health, health);
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        def history = this.testRepeatableWithDecks(deck1, deck2);

        expect:
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
            cards1_.add(new MinionMock("" + i, mana, attack, health, attack, health, health));
            cards2_.add(new MinionMock("" + i, mana, attack, health, attack, health, health));
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        def history = this.testRepeatableWithDecks(deck1, deck2);

        expect:
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
            cards1_.add(new MinionMock("" + i, mana, attack, health, attack, health, health));
            cards2_.add(new MinionMock("" + i, mana, attack, health, attack, health, health));
        }

        Deck deck1 = new Deck(cards1_);
        Deck deck2 = new Deck(cards2_);

        def history = this.testRepeatableWithDecks(deck1, deck2);

        expect:
        assertActionTreeIsRepeatable(history);
    }

    def "game history is repeatable with targetable battlecries"() {
        String[] names1 = ["Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando"];
        String[] names2 = ["Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando","Stormpike Commando"];

        def history = this.testRepeatableWithCardNames(names1, names2);

        expect:
        assertActionTreeIsRepeatable(history);
    }

    def "with flamecannon"() {
        String[] names1 = ["Flamecannon","Bloodfen Raptor","Flamecannon","Bloodfen Raptor","Flamecannon","Bloodfen Raptor","Flamecannon"];
        String[] names2 = ["Bloodfen Raptor","Flamecannon","Bloodfen Raptor","Flamecannon","Bloodfen Raptor","Flamecannon","Bloodfen Raptor"];

        def history = this.testRepeatableWithCardNames(names1, names2);

        expect:
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

    @Ignore("Existing bug")
    def "extra attack until turn end"() {
        String[] names1 = ["Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf"];
        String[] names2 = ["Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf","Dark Iron Dwarf"];

        def history = this.testRepeatableWithCardNames(names1, names2);

        expect:
        assertActionTreeIsRepeatable(history);
    }

    @Ignore("Throwaway")
    // This test is purely for trying to reproduce bugs exposed by the random test below
    def "testGameHistoryWithSpecificDecks"() {
        String[] names1 = ["Bloodlust","Charge","Coldlight Oracle","Rampage","Bananas","Crazed Alchemist","Coldlight Oracle","Arathi Weaponsmith","Malygos","Kill Command","Mana Wyrm","Frost Nova","Twisting Nether","The Coin","Coldlight Seer","Bloodfen Raptor","Magma Rager","Master of Disguise","Wisp","Loot Hoarder","Leper Gnome","Soulfire","Divine Spirit","Sunfury Protector","Cairne Bloodhoof","Dark Iron Dwarf","Argent Squire","Scarlet Crusader","Silence","Stormpike Commando"]
        String[] names2 = ["The Black Knight","Void Terror","Novice Engineer","Alexstrasza","Frost Shock","Nat Pagle","Ancient of Lore","Grimscale Oracle","Earth Elemental","Abusive Sergeant","Stampeding Kodo","Inner Fire","Sludge Belcher","Sunfury Protector","Flamestrike","Ironbeak Owl","Kobold Geomancer","Polymorph","Sprint","Onyxia","Squire","Onyxia","Bloodsail Raider","Silence","Lightning Bolt","Blizzard","Heroic Strike","Boar","Frog","Abomination"]

        def history = this.testRepeatableWithCardNames(names1, names2);

        expect:
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
                current = actionBoardPair.action.perform(current);

                assertNotNull("Should have new node after " + actionBoardPair.action.verb_ + " action", current);
                assertBoardDelta(actionBoardPair.board, current.data_) {
                }
            }
        }
    }
}
