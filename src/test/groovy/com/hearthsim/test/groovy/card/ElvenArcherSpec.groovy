package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.Abomination
import com.hearthsim.card.minion.concrete.ElvenArcher
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import spock.lang.Specification

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class ElvenArcherSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def minionMana = 2;
        def attack = 5;
        def health0 = 3;
        def health1 = 7;

        def commonField = [
                [mana: minionMana, attack: attack, maxHealth: health0], //todo: attack may be irrelevant here
                [mana: minionMana, attack: attack, health: health1 - 1, maxHealth: health1]
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ElvenArcher])
                field(commonField)
                mana(7)
            }
            waitingPlayer {
                field(commonField + [minion: Abomination, health: 1])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }


    def "cannot play for waiting player's side"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = copiedBoard.getCharacter(WAITING_PLAYER, 0)
        def theCard = copiedBoard.getCurrentPlayerCardHand(0)
        def ret = theCard.getCardAction().useOn(WAITING_PLAYER, target, root, null, null)

        expect:

        assertTrue(ret == null)
        assertEquals(copiedBoard, startingBoard)
    }

    def "playing for current player returns expected child states"() {
        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def target = minionPlayedBoard.getCharacter(CURRENT_PLAYER, 2);
        def theCard = minionPlayedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.getCardAction().useOn(CURRENT_PLAYER, target, copiedRoot, null, null);

        expect:
        assertFalse(ret == null);
        assertEquals(ret.numChildren(), 7);

        assertBoardDelta(startingBoard, minionPlayedBoard) {
            currentPlayer {
                playMinion(ElvenArcher)
                mana(6)
            }
        }

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(minionPlayedBoard, child0.data_) {
            currentPlayer {
                heroHealth(29)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(minionPlayedBoard, child1.data_) {
            waitingPlayer {
                heroHealth(29)
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(minionPlayedBoard, child2.data_) {
            currentPlayer {
                updateMinion(0, [deltaHealth: -1])
            }
        }

        HearthTreeNode child3 = ret.getChildren().get(3);
        assertBoardDelta(minionPlayedBoard, child3.data_) {
            currentPlayer {
                updateMinion(1, [deltaHealth: -1])
            }
        }

        HearthTreeNode child4 = ret.getChildren().get(4);
        assertBoardDelta(minionPlayedBoard, child4.data_) {
            waitingPlayer {
                updateMinion(0, [deltaHealth: -1])
            }
        }

        HearthTreeNode child5 = ret.getChildren().get(5);
        assertBoardDelta(minionPlayedBoard, child5.data_) {
            waitingPlayer {
                updateMinion(1, [deltaHealth: -1])
            }
        }

        HearthTreeNode child6 = ret.getChildren().get(6);
        assertBoardDelta(startingBoard, child6.data_) {
            currentPlayer {
                mana(6)
                removeCardFromHand(ElvenArcher)
                heroHealth(28)
                updateMinion(0, [deltaHealth: -2])
                updateMinion(1, [deltaHealth: -2])
            }
            waitingPlayer {
                heroHealth(28)
                removeMinion(2)
                updateMinion(0, [deltaHealth: -2])
                updateMinion(1, [deltaHealth: -2])
            }
        }
    }

}
