package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.card.classic.spell.rare.Blizzard
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Blizzard)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [frozen : true, deltaHealth:-2])
                updateMinion(CharacterIndex.MINION_2, [frozen : true, deltaHealth:-2])
            }
        }
    }
}
