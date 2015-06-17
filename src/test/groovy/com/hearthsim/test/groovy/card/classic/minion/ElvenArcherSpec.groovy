package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.*
import com.hearthsim.card.classic.minion.rare.Abomination
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class ElvenArcherSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ElvenArcher])
                field([[minion:BloodfenRaptor],[minion:WarGolem]])
                mana(7)
            }
            waitingPlayer {
                field([[minion:RiverCrocolisk],[minion:ChillwindYeti],[minion:Abomination, health:1]])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }


    def "cannot play for waiting player's side"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:

        assertTrue(ret == null)
        assertEquals(copiedBoard, startingBoard)
    }

    def "playing for current player returns expected child states"() {
        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, copiedRoot);

        expect:
        assertFalse(ret == null);
        assertEquals(ret.numChildren(), 7);

        assertBoardDelta(startingBoard, minionPlayedBoard) {
            currentPlayer {
                playMinion(ElvenArcher)
                mana(6)
                numCardsUsed(1)
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
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(minionPlayedBoard, child2.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
            }
        }

        HearthTreeNode child3 = ret.getChildren().get(3);
        assertBoardDelta(minionPlayedBoard, child3.data_) {
            waitingPlayer {
                heroHealth(29)
            }
        }

        HearthTreeNode child4 = ret.getChildren().get(4);
        assertBoardDelta(minionPlayedBoard, child4.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }

        HearthTreeNode child5 = ret.getChildren().get(5);
        assertBoardDelta(minionPlayedBoard, child5.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
            }
        }

        HearthTreeNode child6 = ret.getChildren().get(6);
        assertBoardDelta(startingBoard, child6.data_) {
            currentPlayer {
                mana(6)
                removeCardFromHand(ElvenArcher)
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -2])
                removeMinion(CharacterIndex.MINION_1)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(28)
                removeMinion(CharacterIndex.MINION_3)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -2])
            }
        }
    }

}
