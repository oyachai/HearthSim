package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.HolySmite
import com.hearthsim.card.blackrockmountain.minion.common.AxeFlinger
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class AxeFlingerSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([HolySmite])
                field([[minion:AxeFlinger]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:AxeFlinger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "deals damage on damage"() {
        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, copiedRoot);

        expect:
        ret != null

        assertBoardDelta(startingBoard, minionPlayedBoard) {
            currentPlayer {
                removeCardFromHand(HolySmite)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
                mana(9)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(28)
            }
        }
    }

    def "deals damage on opponent's turn"() {
        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, copiedRoot);

        expect:
        ret != null

        assertBoardDelta(startingBoard, minionPlayedBoard) {
            currentPlayer {
                removeCardFromHand(HolySmite)
                mana(9)
                numCardsUsed(1)
                heroHealth(28)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -2])
            }
        }
    }
}
