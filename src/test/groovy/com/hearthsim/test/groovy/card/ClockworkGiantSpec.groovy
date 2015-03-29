package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.ClockworkGiant
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.*


class ClockworkGiantSpec extends CardSpec {

    def "playing Clockwork Giant with no other cards in hand -- can't play"() {

        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ClockworkGiant])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertTrue(ret == null);

    }

    def "playing Clockwork Giant with 3 cards in hand"() {

        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ClockworkGiant])
                mana(10)
            }
            waitingPlayer {
                hand([TheCoin, TheCoin])
                mana(10)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ClockworkGiant)
                mana(0)
                numCardsUsed(1)
            }
        }
    }

}
