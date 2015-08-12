package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BoulderfistOgre
import com.hearthsim.card.basic.minion.ShatteredSunCleric
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals

class ShatteredSunClericSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                field([[minion: BoulderfistOgre]])
                mana(10)
            }
        }
        root = new HearthTreeNode(startingBoard)
    }

    def "adds extra stats"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShatteredSunCleric()
        def ret = theCard.getBattlecryEffect().applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: +1, deltaHealth: +1, deltaMaxHealth: +1])
            }
        }
    }

    def "buff is additive"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShatteredSunCleric()
        theCard.getBattlecryEffect().applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)
        def ret = theCard.getBattlecryEffect().applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: +2, deltaHealth: +2, deltaMaxHealth: +2])
            }
        }
    }
}
