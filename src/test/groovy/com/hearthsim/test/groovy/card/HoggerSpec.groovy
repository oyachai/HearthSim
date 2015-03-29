package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.Gnoll
import com.hearthsim.card.minion.concrete.Hogger
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class HoggerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def minionMana = 2;
        def attack = 5;
        def health0 = 3;

        def commonField = [
                [mana: minionMana, attack: attack, maxHealth: health0], //todo: attack may be irrelevant here
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Hogger])
                field(commonField)
                mana(7)
            }
            waitingPlayer {
                field(commonField)
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }
    
    def "playing Hogger"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Hogger)
                mana(1)
                numCardsUsed(1)
            }
        }

        def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
        assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
            currentPlayer {
                playMinion(Hogger)
                playMinion(Gnoll)
                mana(1)
                numCardsUsed(1)
            }
        }

    }
    
}
