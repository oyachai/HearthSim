package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.classic.minion.legendary.KingMukla
import com.hearthsim.card.classic.spell.common.Bananas
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertFalse

class KingMuklaSpec extends CardSpec {
    
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([KingMukla])
                mana(7)
            }
            waitingPlayer {
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing King Mukla"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:

        assertFalse(ret == null)
        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(KingMukla)
                mana(4)
                numCardsUsed(1)
            }
            waitingPlayer {
                addCardToHand(Bananas)
                addCardToHand(Bananas)
            }
        }
    }
}
