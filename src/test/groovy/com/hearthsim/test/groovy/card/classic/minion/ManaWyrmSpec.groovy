package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.common.ManaWyrm
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class ManaWyrmSpec extends CardSpec {

    def "playing a spell card with a Mana Wyrm on the field"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ManaWyrm, TheCoin])
                mana(9)
            }
            waitingPlayer {
                field([[minion: ManaWyrm]]) //This Mana Wyrm should not be buffed
                mana(9)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def manaWyrm = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = manaWyrm.useOn(CURRENT_PLAYER, 0, root)

        def board2 = new HearthTreeNode(root.data_.deepCopy())
        def theCoin = board2.data_.getCurrentPlayer().getHand().get(0)
        def ret2 = theCoin.useOn(CURRENT_PLAYER, 0, board2)
        
        expect:
        assertFalse(ret == null);
        assertFalse(ret2 == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ManaWyrm)
                mana(8)
                numCardsUsed(1)
            }
        }
        assertBoardDelta(copiedBoard, ret2.data_) {
            currentPlayer {
                playMinion(ManaWyrm)
                removeCardFromHand(TheCoin)
                mana(9)
                updateMinion(0, [deltaAttack: 1])
                numCardsUsed(2)
            }
        }
    }


    def "playing a spell card with a Mana Wyrm in the hand"() {

        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ManaWyrm, TheCoin])
                mana(9)
            }
            waitingPlayer {
                field([[minion: ManaWyrm]]) //This Mana Wyrm should not be buffed
                mana(9)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCoin = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCoin.useOn(CURRENT_PLAYER, 0, root)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                mana(10)
                numCardsUsed(1)
            }
        }
    }
}
