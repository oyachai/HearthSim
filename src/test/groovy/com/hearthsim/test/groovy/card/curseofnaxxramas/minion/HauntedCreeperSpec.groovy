package com.hearthsim.test.groovy.card.curseofnaxxramas.minion

import com.hearthsim.card.CharacterIndex
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
import static org.junit.Assert.*

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
                addMinionToField(SpectralSpider, CharacterIndex.HERO)
                addMinionToField(SpectralSpider, CharacterIndex.HERO)
            }
        }
    }

    def "Haunted creeper death by attacking"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(WAITING_PLAYER).getCharacter(CharacterIndex.MINION_1)
        def hauntedCreeper = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1)
        def ret = hauntedCreeper.attack(WAITING_PLAYER, target, root);

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(SpectralSpider, CharacterIndex.HERO)
                addMinionToField(SpectralSpider, CharacterIndex.HERO)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth:-1])
            }
        }
    }

    def "Haunted creeper kills minion, death by attacking"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(WAITING_PLAYER).getCharacter(CharacterIndex.MINION_1)
        target.setHealth((byte)1)
        def hauntedCreeper = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1)
        def ret = hauntedCreeper.attack(WAITING_PLAYER, target, root);

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(SpectralSpider, CharacterIndex.HERO)
                addMinionToField(SpectralSpider, CharacterIndex.HERO)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
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

        def target = root.data_.modelForSide(WAITING_PLAYER).getCharacter(CharacterIndex.MINION_1)
        target.setHealth((byte)1)
        def hauntedCreeper = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1)
        def ret = hauntedCreeper.attack(WAITING_PLAYER, target, root);

        expect:
        assertNotNull(ret)
        assertEquals(0, ret.numChildren())
        assertFalse(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(SpectralSpider, CharacterIndex.HERO)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
}
