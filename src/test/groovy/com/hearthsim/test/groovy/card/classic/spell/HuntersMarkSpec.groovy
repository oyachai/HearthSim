package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.HuntersMark
import com.hearthsim.card.classic.minion.common.AmaniBerserker
import com.hearthsim.card.classic.spell.common.Silence
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HuntersMark)
                updateMinion(CharacterIndex.MINION_1, [health: 1, maxHealth: 1])
                numCardsUsed(1)
            }
        }
    }

    def "turns off enraged"() {
        def amani = new AmaniBerserker()
        startingBoard.placeMinion(WAITING_PLAYER, amani)
        root = amani.takeDamageAndNotify((byte) 1, CURRENT_PLAYER, WAITING_PLAYER, root, false, false)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = startingBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertFalse(amani.isEnraged())

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HuntersMark)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [health: 1, maxHealth: 1])
            }
        }
    }

    def "silence returns health to normal"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCard = startingBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        theCard = new Silence()
        ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, ret)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HuntersMark)
                numCardsUsed(2)
                updateMinion(CharacterIndex.MINION_1, [silenced:true])
            }
        }
    }
}
