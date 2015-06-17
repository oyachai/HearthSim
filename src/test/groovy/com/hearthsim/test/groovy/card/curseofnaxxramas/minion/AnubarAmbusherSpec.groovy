package com.hearthsim.test.groovy.card.curseofnaxxramas.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.RiverCrocolisk
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.curseofnaxxramas.minion.common.AnubarAmbusher
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class AnubarAmbusherSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion:AnubarAmbusher]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "dying with no deathrattle targets"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new Fireball()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                mana(6)
                numCardsUsed(1)
            }
        }
    }

    def "does not bounce enemies"() {
        startingBoard.placeMinion(WAITING_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new Fireball()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                mana(6)
                numCardsUsed(1)
            }
        }
    }

    def "dying with 1 deathrattle target"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new Fireball()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
                mana(6)
                numCardsUsed(1)
                addCardToHand(BloodfenRaptor)
            }
        }
    }

    def "bouncing with full hand"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())
        startingBoard.placeCardHand(CURRENT_PLAYER, new TheCoin())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new Fireball()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
                mana(6)
                numCardsUsed(1)
            }
        }
    }

    def "dying with 2 deathrattle target"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)
        startingBoard.placeMinion(CURRENT_PLAYER, new RiverCrocolisk(), CharacterIndex.MINION_1)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new Fireball()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_3, root)

        expect:
        assertNotNull(ret)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_3)
                mana(6)
                numCardsUsed(1)
            }
        }

        assertTrue(ret instanceof RandomEffectNode)
        assertEquals(ret.numChildren(), 2)

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addCardToHand(BloodfenRaptor)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_2)
                addCardToHand(RiverCrocolisk)
            }
        }
    }
}
