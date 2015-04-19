package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.goblinsvsgnomes.minion.legendary.Voljin
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class VoljinSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Voljin])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "successfully summoned with no target"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret.numChildren() == 0

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Voljin)
                mana(5)
                numCardsUsed(1)
            }
        }
    }

    def "swaps health with friendly minion"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BoulderfistOgre())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret.numChildren() == 0

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Voljin, 0)
                mana(5)
                numCardsUsed(1)
                updateMinion(0, [health:7, maxHealth:7])
                updateMinion(1, [health:2, maxHealth:2])
            }
        }
    }

    def "swaps health with enemy minion"() {
        startingBoard.placeMinion(WAITING_PLAYER, new BoulderfistOgre())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret.numChildren() == 0

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Voljin, 0)
                mana(5)
                numCardsUsed(1)
                updateMinion(0, [health:7, maxHealth:7])
            }
            waitingPlayer {
                updateMinion(0, [health:2, maxHealth:2])
            }
        }
    }
}
