package com.hearthsim.test.groovy.card.spell

import com.hearthsim.card.spellcard.concrete.LightningBolt
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class LightningBoltSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([LightningBolt])
                mana(5)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def 'gives windfury and overload'(){
        def copiedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(copiedBoard)
        def theCard = copiedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(WAITING_PLAYER, 0, copiedRoot);

        expect:
        ret != null
        assertBoardDelta(startingBoard, copiedBoard) {
            currentPlayer {
                mana(4)
                overload(1)
                removeCardFromHand(LightningBolt)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(27)
            }
        }
    }
}
