package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.classic.minion.epic.SeaGiant
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse


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
