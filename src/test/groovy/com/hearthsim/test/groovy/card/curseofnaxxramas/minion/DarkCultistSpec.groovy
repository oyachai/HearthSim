package com.hearthsim.test.groovy.card.curseofnaxxramas.minion

import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.RiverCrocolisk
import com.hearthsim.card.basic.spell.ShadowWordPain
import com.hearthsim.card.curseofnaxxramas.minion.common.DarkCultist
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class DarkCultistSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion:DarkCultist]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "dying with no deathrattle targets"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(0)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "does not buff enemies"() {
        startingBoard.placeMinion(WAITING_PLAYER, new BloodfenRaptor(), 0)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(0)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "dying with 1 deathrattle target"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), 0)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, 2, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(1)
                mana(8)
                numCardsUsed(1)
                updateMinion(0, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }
    }

    def "dying with 2 deathrattle target"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), 0)
        startingBoard.placeMinion(CURRENT_PLAYER, new RiverCrocolisk(), 1)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, 3, root)

        expect:
        assertNotNull(ret)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(2)
                mana(8)
                numCardsUsed(1)
            }
        }

        assertTrue(ret instanceof RandomEffectNode)
        assertEquals(ret.numChildren(), 2)

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                updateMinion(0, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                updateMinion(1, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }
    }
}
