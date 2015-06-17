package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.ShadowWordDeath
import com.hearthsim.card.classic.minion.common.Lightspawn
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

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
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, root.data_) {
            currentPlayer {
                removeCardFromHand(ShadowWordDeath)
                mana(2)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
    
    def "Damaged Lightspawn is not killed by a Shadow Word: Death"() {
        root.data_.getWaitingPlayer().getCharacter(CharacterIndex.MINION_1).health_ = 4

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertTrue(ret == null);
    }
}
