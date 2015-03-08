package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.SeaGiant
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.card.minion.concrete.GoldshireFootman
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*


class SeaGiantSpec extends CardSpec {

    def "playing Sea Giant with no other minions on board"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SeaGiant])
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
                playMinion(SeaGiant)
                mana(0)
                numCardsUsed(1)
            }
        }

    }

    def "playing Sea Giant with 2 other minions on board"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SeaGiant,])
                field([[minion: GoldshireFootman]])
                mana(10)
            }
            waitingPlayer {
                field([[minion : GoldshireFootman,]])
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
                playMinion(SeaGiant)
                mana(2)
                numCardsUsed(1)
            }
        }
    }

}
