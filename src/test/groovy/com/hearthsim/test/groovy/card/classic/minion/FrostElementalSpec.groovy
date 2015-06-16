package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.FrostElemental
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

class FrostElementalSpec extends CardSpec {

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
                hand([FrostElemental])
                field(commonField)
                mana(7)
            }
            waitingPlayer {
                field(commonField)
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }


    def "playing for current player returns expected child states"() {
        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, copiedRoot);

        expect:
        assertFalse(ret == null);
        assertEquals(ret.numChildren(), 3);

        assertBoardDelta(startingBoard, minionPlayedBoard) {
            currentPlayer {
                playMinion(FrostElemental)
                mana(1)
                numCardsUsed(1)
            }
        }

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(minionPlayedBoard, child0.data_) {
            waitingPlayer {
                heroFrozen(true)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(minionPlayedBoard, child1.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [frozen : true])
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(minionPlayedBoard, child2.data_) {
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_2, [frozen : true])
            }
        }

    }

}
