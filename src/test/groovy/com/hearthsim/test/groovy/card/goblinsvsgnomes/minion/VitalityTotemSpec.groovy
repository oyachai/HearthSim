package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.Game
import com.hearthsim.card.classic.minion.legendary.RagnarosTheFirelord
import com.hearthsim.card.goblinsvsgnomes.minion.rare.VitalityTotem
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

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
