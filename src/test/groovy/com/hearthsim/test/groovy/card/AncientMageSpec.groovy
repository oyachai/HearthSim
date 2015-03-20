package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.AncientMage
import com.hearthsim.card.minion.concrete.SaltyDog
import com.hearthsim.card.minion.concrete.SootSpewer
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class AncientMageSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([AncientMage])
                field([[minion:SaltyDog],[minion:SootSpewer],[minion:SaltyDog]])
                mana(7)
            }
            waitingPlayer {
                field([[minion:SaltyDog],[minion:SootSpewer],[minion:SaltyDog]])
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Ancient Mage on the left edge"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage, 0)
                mana(3)
                updateMinion(1, [deltaSpellDamage: 1])
                numCardsUsed(1)
            }
        }

    }

    def "playing Ancient Mage the middle"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage, 1)
                mana(3)
                updateMinion(0, [deltaSpellDamage: 1])
                updateMinion(2, [deltaSpellDamage: 1])
                numCardsUsed(1)
            }
        }

    }
    def "playing Ancient Mage on the right edge"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 3, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage, 3)
                mana(3)
                updateMinion(2, [deltaSpellDamage: 1])
                numCardsUsed(1)
            }
        }

    }

    def "playing Ancient Mage on empty board"() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([AncientMage])
                mana(7)
            }
        }
        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientMage)
                mana(3)
                numCardsUsed(1)
            }
        }
    }
}
