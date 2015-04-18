package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.MicroMachine
import com.hearthsim.card.minion.concrete.ShadeOfNaxxramas
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

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
                updateMinion(0, [hasAttacked: false, hasBeenUsed: false, deltaAttack: 1])
                addDeckPos(1)
            }
        }
    }
}
