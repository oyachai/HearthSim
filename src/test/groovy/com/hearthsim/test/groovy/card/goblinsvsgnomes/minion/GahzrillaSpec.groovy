package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.basic.spell.Moonfire
import com.hearthsim.card.goblinsvsgnomes.minion.legendary.Gahzrilla
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class GahzrillaSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Moonfire, Moonfire])
                field([[minion:Gahzrilla]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "buffs after takes damage"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Moonfire)
                numCardsUsed(1)
                updateMinion(0, [attack: 12, deltaHealth: -1])
            }
        }
    }

    def "buffs stack"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def first = theCard.useOn(CURRENT_PLAYER, 1, root)
        def copiedBoard = first.data_.deepCopy()

        theCard = first.data_.getCurrentPlayer().getHand().get(0)
        def second = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        second != null

        assertBoardDelta(copiedBoard, second.data_) {
            currentPlayer {
                removeCardFromHand(Moonfire)
                numCardsUsed(2)
                updateMinion(0, [attack: 24, deltaHealth: -1])
            }
        }
    }

    def "does buff if opponent's turn"() {
        startingBoard.removeMinion(CURRENT_PLAYER, 0);
        startingBoard.placeMinion(WAITING_PLAYER, new Gahzrilla());

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Moonfire)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [deltaAttack: 6, deltaHealth: -1])
            }
        }
    }
}
