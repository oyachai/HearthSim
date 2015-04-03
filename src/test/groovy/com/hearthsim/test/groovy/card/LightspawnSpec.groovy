package com.hearthsim.test.groovy.card

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

import com.hearthsim.card.minion.concrete.Lightspawn
import com.hearthsim.card.spellcard.concrete.ShadowWordDeath
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

class LightspawnSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ShadowWordDeath])
                mana(5)
            }
            waitingPlayer {
                field([[minion: Lightspawn]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }
    
    def "Lightspawn is killed by a Shadow Word: Death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, root.data_) {
            currentPlayer {
                removeCardFromHand(ShadowWordDeath)
                mana(2)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }
    
    def "Damaged Lightspawn is not killed by a Shadow Word: Death"() {
        root.data_.getWaitingPlayer().getCharacter(1).health_ = 4

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

        expect:
        assertTrue(ret == null);
    }
}
