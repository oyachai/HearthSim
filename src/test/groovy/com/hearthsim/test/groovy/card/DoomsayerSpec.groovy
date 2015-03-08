package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.Doomsayer
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*


class DoomsayerSpec extends CardSpec {

    def "playing Doomsayer with no other minion"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                deck([TheCoin])
                hand([Doomsayer])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Doomsayer)
                mana(8)
                numCardsUsed(1)
            }
        }
        
        def retAfterStartTurn = new HearthTreeNode(Game.beginTurn(ret.data_))
        assertBoardDelta(copiedBoard, retAfterStartTurn.data_) {
            currentPlayer {
                removeCardFromHand(Doomsayer)
                addCardToHand([TheCoin])
                addDeckPos(1)
            }
        }
    }

    def "playing Doomsayer with other minions"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                deck([TheCoin])
                hand([Doomsayer])
                field([[minion: GoldshireFootman]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: GoldshireFootman]])
                mana(10)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Doomsayer)
                mana(8)
                numCardsUsed(1)
            }
        }
        
        def retAfterStartTurn = new HearthTreeNode(Game.beginTurn(ret.data_))
        assertBoardDelta(copiedBoard, retAfterStartTurn.data_) {
            currentPlayer {
                removeCardFromHand(Doomsayer)
                removeMinion(0)
                addCardToHand([TheCoin])
                addDeckPos(1)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }

    }

}
