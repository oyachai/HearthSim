package com.hearthsim.test.groovy.card

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.card.minion.concrete.ShatteredSunCleric
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

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
        def ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, theCard, CURRENT_PLAYER, 1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [deltaAttack: +1, deltaHealth: +1, deltaMaxHealth: +1])
            }
        }
    }

    def "buff is additive"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new ShatteredSunCleric()
        theCard.useTargetableBattlecry_core(CURRENT_PLAYER, theCard, CURRENT_PLAYER, 1, root)
        def ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, theCard, CURRENT_PLAYER, 1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [deltaAttack: +2, deltaHealth: +2, deltaMaxHealth: +2])
            }
        }
    }
}
