package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.common.Gnoll
import com.hearthsim.card.classic.minion.legendary.Hogger
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

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
