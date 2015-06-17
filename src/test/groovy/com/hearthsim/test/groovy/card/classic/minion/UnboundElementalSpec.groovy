package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.HolySmite
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.common.UnboundElemental
import com.hearthsim.card.classic.spell.common.LightningBolt
import com.hearthsim.card.classic.spell.rare.LavaBurst
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertNotNull

class UnboundElementalSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([UnboundElemental, TheCoin])
                field([[minion: UnboundElemental]])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "buffs after playing overload"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new LightningBolt()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(9)
                overload(1)
                numCardsUsed(1)
                heroHealth(27)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1, deltaHealth: 1, deltaMaxHealth: 1])
            }
        }
    }

    def "buffs after playing overload 2"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new LavaBurst()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(7)
                overload(2)
                numCardsUsed(1)
                heroHealth(25)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1, deltaHealth: 1, deltaMaxHealth: 1])
            }
        }
    }

    def "does not buff after playing non-overload"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new HolySmite()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(9)
                numCardsUsed(1)
                heroHealth(28)
            }
        }
    }

    def "does not buff on enemy overload"() {
        root.data_.placeMinion(WAITING_PLAYER, new UnboundElemental())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = new LightningBolt()
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertNotNull(ret);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(9)
                overload(1)
                numCardsUsed(1)
                heroHealth(27)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 1, deltaHealth: 1, deltaMaxHealth: 1])
            }
        }
    }
}
