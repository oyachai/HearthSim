package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.epic.Doomsayer
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse


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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

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
                removeMinion(CharacterIndex.MINION_1)
                addCardToHand([TheCoin])
                addDeckPos(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }

    }

}
