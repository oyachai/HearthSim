package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.AntiqueHealbot
import com.hearthsim.card.minion.concrete.BloodfenRaptor
import com.hearthsim.card.minion.concrete.FlameImp
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class AntiqueHealbotSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                heroHealth(10)
                hand([AntiqueHealbot])
                field([[minion:BloodfenRaptor]])
                mana(7)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing FlameImp damages the hero"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AntiqueHealbot)
                mana(2)
                heroHealth(18)
                numCardsUsed(1)
            }
        }

    }
}
