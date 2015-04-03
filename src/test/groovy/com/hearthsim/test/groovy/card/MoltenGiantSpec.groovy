package com.hearthsim.test.groovy.card

import com.hearthsim.card.Card
import com.hearthsim.card.Deck
import com.hearthsim.card.minion.concrete.MoltenGiant
import com.hearthsim.card.spellcard.concrete.TheCoin
import com.hearthsim.card.spellcard.concrete.HolySmite
import com.hearthsim.model.BoardModel
import com.hearthsim.Game
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*


class MoltenGiantSpec extends CardSpec {

    def "playing Molten Giant with full health -- cant't play"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MoltenGiant])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertTrue(ret == null);

    }

    def "playing Molten Giant with 15 health"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MoltenGiant])
                heroHealth(15)
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
                playMinion(MoltenGiant)
                mana(5)
                numCardsUsed(1)
            }
        }
    }

    def "playing Molten Giant with 7 health"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MoltenGiant])
                heroHealth(7)
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
                playMinion(MoltenGiant)
                mana(10)
                numCardsUsed(1)
            }
        }
    }

}
