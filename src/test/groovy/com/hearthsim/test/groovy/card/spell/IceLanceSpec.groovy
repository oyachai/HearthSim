package com.hearthsim.test.groovy.card.spell

import com.hearthsim.card.minion.concrete.KoboldGeomancer
import com.hearthsim.test.groovy.card.CardSpec

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.StranglethornTiger
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.spellcard.concrete.IceLance
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class IceLanceSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([IceLance])
                mana(1)
            }
            waitingPlayer {
                field([[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "freezes unfrozen minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(IceLance)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [frozen : true])
            }
        }
    }

    def "damages frozen minion"() {
        startingBoard.getMinion(WAITING_PLAYER, 0).setFrozen(true)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(IceLance)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -4])
            }
        }
    }

    def "affected by spellpower"() {
        startingBoard.getMinion(WAITING_PLAYER, 0).setFrozen(true)
        startingBoard.placeMinion(CURRENT_PLAYER, new KoboldGeomancer())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(IceLance)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -5])
            }
        }
    }
}
