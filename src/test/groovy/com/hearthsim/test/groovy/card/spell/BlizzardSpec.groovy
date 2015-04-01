package com.hearthsim.test.groovy.card.spell

import com.hearthsim.test.groovy.card.CardSpec

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.StranglethornTiger
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.spellcard.concrete.Blizzard
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class BlizzardSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Blizzard])
                mana(6)
            }
            waitingPlayer {
                field([[minion: WarGolem],[minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "freezes unfrozen minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(WAITING_PLAYER, 0, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Blizzard)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [frozen : true, deltaHealth:-2])
                updateMinion(1, [frozen : true, deltaHealth:-2])
            }
        }
    }
}
