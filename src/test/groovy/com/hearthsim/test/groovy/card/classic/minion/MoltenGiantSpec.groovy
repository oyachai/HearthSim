package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.epic.MoltenGiant
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue


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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
