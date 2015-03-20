package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.MasterOfDisguise

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.BoulderfistOgre
import com.hearthsim.card.minion.concrete.ShatteredSunCleric
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class MasterOfDisguiseSpec extends CardSpec {

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

    def "adds stealth"() {
        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(1)
        def theCard = new MasterOfDisguise()
        def ret = theCard.useTargetableBattlecry_core(CURRENT_PLAYER, target, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(0, [stealthed: true])
            }
        }
    }
}
