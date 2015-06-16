package com.hearthsim.test.groovy.card.curseofnaxxramas.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.RiverCrocolisk
import com.hearthsim.card.basic.spell.ShadowWordPain
import com.hearthsim.card.classic.minion.epic.BigGameHunter
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
            waitingPlayer {
                field([[minion:BigGameHunter]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "dying with no deathrattle targets"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "does not buff enemies"() {
        startingBoard.placeMinion(WAITING_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "dying with 1 deathrattle target"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_2)
                mana(8)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }
    }

    def "dying with 2 deathrattle target"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)
        startingBoard.placeMinion(CURRENT_PLAYER, new RiverCrocolisk(), CharacterIndex.MINION_1)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_3, root)

        expect:
        assertNotNull(ret)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_3)
                mana(8)
                numCardsUsed(1)
            }
        }

        assertTrue(ret instanceof RandomEffectNode)
        assertEquals(ret.numChildren(), 2)

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }
    }

    def "dying with 2 deathrattle target, dark cultist in the middle"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)
        startingBoard.placeMinion(CURRENT_PLAYER, new RiverCrocolisk(), CharacterIndex.MINION_2)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShadowWordPain()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertNotNull(ret)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_2)
                mana(8)
                numCardsUsed(1)
            }
        }

        assertTrue(ret instanceof RandomEffectNode)
        assertEquals(ret.numChildren(), 2)

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }
    }

    def "dying with 2 deathrattle target, dark cultist dies by attacking"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new BloodfenRaptor(), CharacterIndex.HERO)
        startingBoard.placeMinion(CURRENT_PLAYER, new RiverCrocolisk(), CharacterIndex.MINION_2)

        def copiedBoard = startingBoard.deepCopy()
        def darkCultist = root.data_.getCurrentPlayer().getCharacter(CharacterIndex.MINION_2)
        def ret = darkCultist.attack(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNotNull(ret)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_2)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }

        assertTrue(ret instanceof RandomEffectNode)
        assertEquals(ret.numChildren(), 2)

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(ret.data_, child1.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaMaxHealth: +3, deltaHealth: +3])
            }
        }
    }
}
