package com.hearthsim.test.groovy.card.curseofnaxxramas.minion

import com.hearthsim.card.basic.spell.ShadowWordPain
import com.hearthsim.card.curseofnaxxramas.minion.common.HauntedCreeper
import com.hearthsim.card.curseofnaxxramas.minion.common.SpectralSpider
import com.hearthsim.card.curseofnaxxramas.minion.common.ZombieChow
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull

/**
 * Created by oyachai on 4/25/15.
 */
class HauntedCreeperSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion:HauntedCreeper]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:ZombieChow]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "Haunted creeper dying"() {
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
                addMinionToField(SpectralSpider, 0)
                addMinionToField(SpectralSpider, 0)
            }
        }
    }

    def "Haunted creeper death by attacking"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(WAITING_PLAYER).getCharacter(1)
        def hauntedCreeper = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def ret = hauntedCreeper.attack(WAITING_PLAYER, target, root);

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(0)
                addMinionToField(SpectralSpider, 0)
                addMinionToField(SpectralSpider, 0)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth:-1])
            }
        }
    }

    def "Haunted creeper kills minion, death by attacking"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(WAITING_PLAYER).getCharacter(1)
        target.setHealth((byte)1)
        def hauntedCreeper = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def ret = hauntedCreeper.attack(WAITING_PLAYER, target, root);

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(0)
                addMinionToField(SpectralSpider, 0)
                addMinionToField(SpectralSpider, 0)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }

    def "Haunted creeper kills minion, with full board"() {

        root.data_.placeMinion(CURRENT_PLAYER, new HauntedCreeper());
        root.data_.placeMinion(CURRENT_PLAYER, new HauntedCreeper());
        root.data_.placeMinion(CURRENT_PLAYER, new HauntedCreeper());
        root.data_.placeMinion(CURRENT_PLAYER, new HauntedCreeper());
        root.data_.placeMinion(CURRENT_PLAYER, new HauntedCreeper());
        root.data_.placeMinion(CURRENT_PLAYER, new HauntedCreeper());

        def copiedBoard = startingBoard.deepCopy()

        def target = root.data_.modelForSide(WAITING_PLAYER).getCharacter(1)
        target.setHealth((byte)1)
        def hauntedCreeper = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def ret = hauntedCreeper.attack(WAITING_PLAYER, target, root);

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(0)
                addMinionToField(SpectralSpider, 0)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }
}
