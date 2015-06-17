package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.Sap
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals

class SapSpec extends CardSpec {
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Sap])
                mana(7)
            }
            waitingPlayer {
                field([[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "sap enemy minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret)

        assertBoardDelta(copiedBoard, root.data_) {
            currentPlayer {
                removeCardFromHand(Sap)
                mana(5)
                numCardsUsed(1)
            }
            waitingPlayer {
                addCardToHand(WarGolem)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

    def "minion destroyed if hand full"() {
        for (int indx = 0; indx < 10; ++indx) {
            startingBoard.getWaitingPlayer().placeCardHand(new TheCoin());
        }

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret)

        assertBoardDelta(copiedBoard, root.data_) {
            currentPlayer {
                removeCardFromHand(Sap)
                mana(5)
                numCardsUsed(1)
            }
            waitingPlayer { removeMinion(CharacterIndex.MINION_1) }
        }
    }
}
