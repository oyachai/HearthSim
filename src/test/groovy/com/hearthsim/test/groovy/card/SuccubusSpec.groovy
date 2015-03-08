package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.Succubus
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.card.spellcard.concrete.HolySmite
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class SuccubusSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def "playing Succubus with no other cards in hand"() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Succubus])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(0)
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Succubus)
                mana(5)
                numCardsUsed(1)
            }
        }
    }

    
    def "playing Succubus with no one other card in hand"() {
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Succubus, TheCoin])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(0)
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Succubus)
                mana(5)
                numCardsUsed(1)
            }
        }
        
        assertEquals(ret.numChildren(), 1);
        
        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinion(Succubus)
                mana(5)
                removeCardFromHand(TheCoin)
                numCardsUsed(1)
            }
        }

    }
        
    
    
    
    def "playing Succubus with two other cards in hand"() {
        
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Succubus, TheCoin, HolySmite])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def target = root.data_.modelForSide(CURRENT_PLAYER).getCharacter(0)
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, target, root, null, null)

        expect:
        assertFalse(ret == null);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Succubus)
                mana(5)
                numCardsUsed(1)
            }
        }
        
        assertEquals(ret.numChildren(), 2);
        
        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinion(Succubus)
                mana(5)
                removeCardFromHand(TheCoin)
                numCardsUsed(1)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                playMinion(Succubus)
                mana(5)
                removeCardFromHand(HolySmite)
                numCardsUsed(1)
            }
        }

    }
}
