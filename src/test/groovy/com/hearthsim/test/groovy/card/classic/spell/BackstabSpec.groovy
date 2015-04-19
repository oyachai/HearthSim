package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.Backstab
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

class BackstabSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Backstab])
                mana(0)
            }
            waitingPlayer {
                field([[minion: WarGolem], [minion: WarGolem, health: 3], [minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "can target undamaged minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Backstab)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -2])
            }
        }
    }

    def "cannot target damaged minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 2, root, null, null)

        expect:
        assertNull(ret);

        assertBoardEquals(copiedBoard, root.data_)
    }
    
    def "follows normal targeting rules"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 3, root, null, null)

        expect:
        assertNull(ret);

        assertBoardEquals(copiedBoard, root.data_)
    }
}
