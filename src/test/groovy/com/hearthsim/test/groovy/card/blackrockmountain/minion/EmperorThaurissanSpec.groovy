package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.blackrockmountain.minion.legendary.EmperorThaurissan
import com.hearthsim.card.classic.minion.common.Wisp
import com.hearthsim.card.goblinsvsgnomes.minion.common.SpiderTank
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class EmperorThaurissanSpec extends CardSpec{

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([EmperorThaurissan, SpiderTank, BloodfenRaptor])
                mana(10)
            }
            waitingPlayer {
                hand([EmperorThaurissan, SpiderTank, BloodfenRaptor])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "at the end of currentPlayer turn, reduce the Cost of currentPlayer cards by 1"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(EmperorThaurissan)
                mana(4)
                numCardsUsed(1)
            }
        }

        def retAfterEndTurn = new HearthTreeNode(Game.endTurn(ret.data_))
        assertBoardDelta(copiedBoard, retAfterEndTurn.data_) {
            currentPlayer {
                playMinion(EmperorThaurissan)
                mana(4)
                numCardsUsed(1)
                updateCardInHand(0, [manaDelta: -1])
                updateCardInHand(1, [manaDelta: -1])
            }
            waitingPlayer {
                updateCardInHand(0, [manaDelta: 0])
                updateCardInHand(1, [manaDelta: 0])
                updateCardInHand(2, [manaDelta: 0])
            }
        }

    }
}
