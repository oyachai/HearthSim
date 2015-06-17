package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.epic.MountainGiant
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue


class MountainGiantSpec extends CardSpec {

    def "playing Mountain Giant with no other cards in hand -- can't play"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MountainGiant])
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

    def "playing Mountain Giant with 2 other cards in hand"() {
        
        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MountainGiant, TheCoin, TheCoin])
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
                playMinion(MountainGiant)
                mana(0)
                numCardsUsed(1)
            }
        }
    }

}
