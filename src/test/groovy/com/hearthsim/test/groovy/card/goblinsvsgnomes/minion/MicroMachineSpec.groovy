package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.goblinsvsgnomes.minion.common.MicroMachine
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class MicroMachineSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion:MicroMachine]])
                mana(10)
                deck([TheCoin, TheCoin])
            }
            waitingPlayer {
                deck([TheCoin, TheCoin])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "buffs on turn begin"() {
        def copiedBoard = startingBoard.deepCopy()
        def retAfterStartTurn = new HearthTreeNode(Game.beginTurn(startingBoard))

        expect:

        assertBoardDelta(copiedBoard, retAfterStartTurn.data_) {
            currentPlayer {
                addCardToHand(TheCoin)
                updateMinion(CharacterIndex.MINION_1, [hasAttacked: false, hasBeenUsed: false, deltaAttack: 1])
                addDeckPos(1)
            }
        }
    }
}
