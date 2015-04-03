package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.DefiasRingleader
import com.hearthsim.card.minion.concrete.DefiasBandit
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

/**
 * Created by oyachai on 3/21/15.
 */
class DefiasRingleaderSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TheCoin, DefiasRingleader])
                mana(2)
            }
            waitingPlayer {
                mana(1)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "Playing Defias Ringleader without combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(DefiasRingleader, 0)
                numCardsUsed(1)
                mana(0)
            }
        }
    }

    def "Playing Defias Ringleader with combo"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCoin = root.data_.getCurrentPlayer().getHand().get(0)
        theCoin.useOn(CURRENT_PLAYER, 0, root)

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                playMinion(DefiasRingleader, 0)
                playMinion(DefiasBandit, 1)
                numCardsUsed(2)
                mana(1)
            }
        }
    }

}
