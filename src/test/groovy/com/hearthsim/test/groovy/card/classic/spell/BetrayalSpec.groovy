package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.spell.common.Betrayal
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

/**
 * Created by oyachai on 8/12/15.
 */
class BetrayalSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Betrayal])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "Betrayal with one target on the board"() {
        startingBoard.placeMinion(WAITING_PLAYER, new WarGolem());

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret == null
    }

    def "Betrayal with two target on the board, target left minion"() {
        startingBoard.placeMinion(WAITING_PLAYER, new WarGolem());
        startingBoard.placeMinion(WAITING_PLAYER, new GoldshireFootman());

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Betrayal)
                mana(8)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_2)
            }
        }

    }

    def "Betrayal with two target on the board, target right minion"() {
        startingBoard.placeMinion(WAITING_PLAYER, new WarGolem());
        startingBoard.placeMinion(WAITING_PLAYER, new GoldshireFootman());

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Betrayal)
                mana(8)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }
    }

    def "Betrayal with three target on the board, target middle minion"() {
        startingBoard.placeMinion(WAITING_PLAYER, new WarGolem());
        startingBoard.placeMinion(WAITING_PLAYER, new GoldshireFootman());
        startingBoard.placeMinion(WAITING_PLAYER, new GoldshireFootman());

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Betrayal)
                mana(8)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
                updateMinion(CharacterIndex.MINION_3, [deltaHealth: -1])
            }
        }

    }
}
