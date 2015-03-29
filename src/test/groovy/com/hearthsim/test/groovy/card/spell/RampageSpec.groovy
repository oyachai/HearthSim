package com.hearthsim.test.groovy.card.spell

import com.hearthsim.test.groovy.card.CardSpec

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.StranglethornTiger
import com.hearthsim.card.minion.concrete.WarGolem
import com.hearthsim.card.spellcard.concrete.Rampage
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class RampageSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Rampage])
                field([[minion: WarGolem], [minion: WarGolem, health: 3]])
                mana(3)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "can target damaged minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 2, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Rampage)
                mana(1)
                updateMinion(1, [deltaHealth: +3, deltaAttack: +3, deltaMaxHealth: +3])
                numCardsUsed(1)
            }
        }
    }

    def "cannot target undamaged minion"() {
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

        expect:
        assertNull(ret);
    }
}
