package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.goblinsvsgnomes.minion.epic.Shadowbomber
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class ShadowbomberSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Shadowbomber])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "damages both heroes"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Shadowbomber)
                mana(9)
                heroHealth(27)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(27)
            }
        }
    }
}
