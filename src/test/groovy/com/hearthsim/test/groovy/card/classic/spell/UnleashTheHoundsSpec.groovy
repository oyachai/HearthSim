package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.classic.minion.common.Hound
import com.hearthsim.card.classic.spell.common.UnleashTheHounds
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull

class UnleashTheHoundsSpec extends CardSpec {
    
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def minionMana = 2;
        def attack = 5;
        def health0 = 3;

        def commonField = [
            [mana: minionMana, attack: attack, maxHealth: health0], //TODO: attack may be irrelevant here
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([UnleashTheHounds])
                mana(7)
            }
            waitingPlayer {
                field(commonField)
                mana(4)
            }
        }

        
        root = new HearthTreeNode(startingBoard)
    }

    def "playing UnleashTheHounds with 1 enemy minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(UnleashTheHounds)
                addMinionToField(Hound, false, true)
                mana(4)
                numCardsUsed(1)
            }
        }

    }

    def "playing UnleashTheHounds with not enough room"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(WAITING_PLAYER, new BloodfenRaptor());

        def copiedBoard = startingBoard.deepCopy()

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(UnleashTheHounds)
                addMinionToField(Hound, false, true)
                mana(4)
                numCardsUsed(1)
            }
        }

    }
}
