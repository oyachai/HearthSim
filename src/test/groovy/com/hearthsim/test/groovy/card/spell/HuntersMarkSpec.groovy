package com.hearthsim.test.groovy.card.spell

import com.hearthsim.card.minion.concrete.AmaniBerserker
import com.hearthsim.card.spellcard.concrete.HuntersMark
import com.hearthsim.card.spellcard.concrete.Silence
import com.hearthsim.test.groovy.card.CardSpec
import spock.lang.Ignore

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class HuntersMarkSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([HuntersMark])
                field([[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "sets health to 1"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = startingBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HuntersMark)
                updateMinion(0, [health: 1, maxHealth: 1])
                numCardsUsed(1)
            }
        }
    }

    @Ignore("Existing bug")
    def "turns off enraged"() {
        def amani = new AmaniBerserker()
        startingBoard.placeMinion(WAITING_PLAYER, amani)
        root = amani.takeDamageAndNotify((byte) 1, CURRENT_PLAYER, WAITING_PLAYER, root, false, false)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = startingBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(WAITING_PLAYER, 1, root)

        expect:
        assertEquals(root, ret);

        assertFalse(amani.isEnraged())

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HuntersMark)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [health: 1, maxHealth: 1])
            }
        }
    }

    @Ignore("Existing bug")
    def "silence returns health to normal"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCard = startingBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        theCard = new Silence()
        ret = theCard.useOn(CURRENT_PLAYER, 1, ret)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HuntersMark)
                numCardsUsed(2)
            }
        }
    }
}
