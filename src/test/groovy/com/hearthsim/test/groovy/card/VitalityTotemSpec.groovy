package com.hearthsim.test.groovy.card

import com.hearthsim.Game
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.card.minion.concrete.RagnarosTheFirelord
import com.hearthsim.card.minion.concrete.VitalityTotem
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse


/**
 * Created by oyachai on 4/5/15.
 */
class VitalityTotemSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                heroHealth(10)
                field([[minion: VitalityTotem]])
                hand([RagnarosTheFirelord])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "heals after own turn"() {
        def copiedBoard = startingBoard.deepCopy()
        def retAfterEndTurn = new HearthTreeNode(Game.endTurn(startingBoard))

        expect:
        assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
            currentPlayer {
                heroHealth(14)
            }
        }
    }
}
