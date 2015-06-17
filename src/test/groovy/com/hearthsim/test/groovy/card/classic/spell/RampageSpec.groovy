package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.spell.common.Rampage
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Rampage)
                mana(1)
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: +3, deltaAttack: +3, deltaMaxHealth: +3])
                numCardsUsed(1)
            }
        }
    }

    def "cannot target undamaged minion"() {
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertNull(ret);
    }
}
