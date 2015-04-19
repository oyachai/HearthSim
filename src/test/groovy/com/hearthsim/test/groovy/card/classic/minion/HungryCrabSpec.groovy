package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.MurlocRaider
import com.hearthsim.card.classic.minion.epic.HungryCrab
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class HungryCrabSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([HungryCrab])
                mana(2)
            }

            waitingPlayer {
                mana(2)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Hungry Crab without a murloc does not get buff"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertNotNull(ret);
        assertEquals(ret.numChildren(), 0);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(HungryCrab)
                mana(1)
                numCardsUsed(1)
            }
        }
    }

    def "kills enemy murloc"() {
        startingBoard.placeMinion(WAITING_PLAYER, new MurlocRaider())

        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot);

        expect:
        assertNotNull(ret);
        assertEquals(ret.numChildren(), 0);

        assertBoardDelta(startingBoard, ret.data_) {
            currentPlayer {
                playMinion(HungryCrab)
                mana(1)
                numCardsUsed(1)
                updateMinion(0, [deltaAttack: +2, deltaHealth: +2, deltaMaxHealth: +2])
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }

    def "kills own murloc"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new MurlocRaider())

        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot);

        expect:
        assertNotNull(ret);
        assertEquals(ret.numChildren(), 0);

        assertBoardDelta(startingBoard, ret.data_) {
            currentPlayer {
                playMinion(HungryCrab, 0)
                mana(1)
                numCardsUsed(1)
                updateMinion(0, [deltaAttack: +2, deltaHealth: +2, deltaMaxHealth: +2])
                removeMinion(1)
            }
        }
    }

    def "does not kill non-murloc"() {
        startingBoard.placeMinion(WAITING_PLAYER, new BloodfenRaptor())

        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayerCardHand(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 0, copiedRoot);

        expect:
        assertNotNull(ret);
        assertEquals(ret.numChildren(), 0);

        assertBoardDelta(startingBoard, minionPlayedBoard) {
            currentPlayer {
                playMinion(HungryCrab)
                mana(1)
                numCardsUsed(1)
            }
        }
    }
}
