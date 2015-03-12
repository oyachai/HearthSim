package com.hearthsim.test.groovy.card

import com.hearthsim.model.BoardModel;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;

import com.hearthsim.card.minion.concrete.KingMukla
import com.hearthsim.card.spellcard.concrete.Bananas

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class KingMuklaSpec extends CardSpec {
    
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([KingMukla])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing King Mukla"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:

        assertFalse(ret == null)
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(KingMukla)
                mana(4)
                numCardsUsed(1)
            }
            waitingPlayer {
                addCardToHand(Bananas)
                addCardToHand(Bananas)
            }
        }
    }
}
