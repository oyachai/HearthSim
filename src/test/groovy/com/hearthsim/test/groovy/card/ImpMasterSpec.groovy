package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.ImpMaster
import com.hearthsim.card.minion.concrete.Imp
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

public class ImpMasterSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ImpMaster])
                mana(8)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }
    
    def "playing Imp Master"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ImpMaster)
                mana(5)
                numCardsUsed(1)
            }
        }

        def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
        assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
            currentPlayer {
                playMinion(ImpMaster)
                addMinionToField(Imp)
                mana(5)
                updateMinion(0, [deltaHealth: -1])
                numCardsUsed(1)
            }
        }
    }
    
    def "playing Imp Master with 1 health left"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)
        theCard.health_ = 1
        
        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ImpMaster)
                updateMinion(0, [deltaHealth: -4])
                mana(5)
                numCardsUsed(1)
            }
        }

        def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
        assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
            currentPlayer {
                playMinion(ImpMaster)
                removeMinion(0)
                addMinionToField(Imp)
                mana(5)
                numCardsUsed(1)
            }
        }
    }
}
