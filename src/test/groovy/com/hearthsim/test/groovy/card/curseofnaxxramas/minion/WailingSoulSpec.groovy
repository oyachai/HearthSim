package com.hearthsim.test.groovy.card.curseofnaxxramas.minion

import com.hearthsim.card.classic.minion.rare.AncientWatcher
import com.hearthsim.card.curseofnaxxramas.minion.rare.WailingSoul
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class WailingSoulSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([WailingSoul])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "silences friendly minions"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new AncientWatcher())
        startingBoard.placeMinion(CURRENT_PLAYER, new AncientWatcher())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(WailingSoul, 0)
                mana(6)
                numCardsUsed(1)
                updateMinion(1, [silenced:true])
                updateMinion(2, [silenced:true])
            }
        }
    }

    def "does not silence enemy minions"() {
        startingBoard.placeMinion(WAITING_PLAYER, new AncientWatcher())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(WailingSoul)
                mana(6)
                numCardsUsed(1)
            }
        }
    }
}
