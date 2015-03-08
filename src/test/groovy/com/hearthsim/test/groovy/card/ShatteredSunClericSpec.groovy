package com.hearthsim.test.groovy.card

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.card.minion.concrete.ShatteredSunCleric
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
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
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def theCard = new ShatteredSunCleric()
        def ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, target, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [deltaAttack: +1])
                updateMinion(0, [deltaHealth: +1])
            }
        }
    }

    def "buff is additive"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def theCard = new ShatteredSunCleric()
        def ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, target, root, null, null)
        ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, target, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [deltaAttack: +2])
                updateMinion(0, [deltaHealth: +2])
            }
        }
    }
}
