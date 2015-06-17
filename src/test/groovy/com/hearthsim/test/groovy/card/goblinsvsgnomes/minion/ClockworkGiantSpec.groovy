package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.goblinsvsgnomes.minion.epic.ClockworkGiant
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue


class ClockworkGiantSpec extends CardSpec {

    def "playing Clockwork Giant with no other cards in hand -- can't play"() {

        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ClockworkGiant])
                mana(10)
            }
            waitingPlayer {
                mana(10)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertTrue(ret == null);

    }

    def "playing Clockwork Giant with 3 cards in hand"() {

        def startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ClockworkGiant])
                mana(10)
            }
            waitingPlayer {
                hand([TheCoin, TheCoin])
                mana(10)
            }
        }

        def root = new HearthTreeNode(startingBoard)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayerCardHand(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertFalse(ret == null);
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ClockworkGiant)
                mana(0)
                numCardsUsed(1)
            }
        }
    }

}
