package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.ChillwindYeti
import com.hearthsim.card.basic.minion.GoldshireFootman
import com.hearthsim.card.classic.minion.epic.CabalShadowPriest
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

class CabalShadowPriestSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        def commonField = [
            [minion: GoldshireFootman], //TODO: attack may be irrelevant here
            [minion: ChillwindYeti]
        ]

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([CabalShadowPriest])
                field(commonField)
                mana(7)
            }
            waitingPlayer {
                field(commonField)
                mana(4)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Cabal Shadow Priest"() {
        def minionPlayedBoard = startingBoard.deepCopy()
        def copiedRoot = new HearthTreeNode(minionPlayedBoard)
        def theCard = minionPlayedBoard.getCurrentPlayer().getHand().get(0);
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, copiedRoot);

        expect:
        assertFalse(ret == null);
        assertEquals(ret.numChildren(), 0);

        assertBoardDelta(startingBoard, ret.data_) {
            currentPlayer {
                playMinion(CabalShadowPriest)
                mana(1)
                numCardsUsed(1)
                addMinionToField(GoldshireFootman, true, true, CharacterIndex.MINION_3)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
}
