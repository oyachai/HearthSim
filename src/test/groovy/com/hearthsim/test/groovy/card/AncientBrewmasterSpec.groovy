package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.AncientBrewmaster
import com.hearthsim.card.minion.concrete.StormwindChampion
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

class AncientBrewmasterSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {    
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([AncientBrewmaster])
                mana(7)
            }
            waitingPlayer { mana(7) }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Ancient Brewmaster while there are no other minions no board"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root, null, null)

        expect:
        assertFalse(ret == null)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientBrewmaster)
                mana(3)
                numCardsUsed(1)
            }
        }
        assertEquals(ret.numChildren(), 0)
    }

    def "playing Ancient Brewmaster with one friendly minion on board"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new StormwindChampion())
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

        expect:
        assertFalse(ret == null)
        assertEquals(ret.numChildren(), 1)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientBrewmaster)
                mana(3)
                numCardsUsed(1)
            }
        }

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                addCardToHand(StormwindChampion)
                removeMinion(0)
            }
        }
    }

    def "bounced minion does not remember state"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new StormwindChampion())
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(1).health = 2
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(1).attack = 2
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(1).divineShield = true
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(1).windfury = true
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(1).spellDamage = 4

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root, null, null)

        expect:
        assertFalse(ret == null)
        assertEquals(ret.numChildren(), 1)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientBrewmaster)
                mana(3)
                numCardsUsed(1)
            }
        }

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(ret.data_, child0.data_) {
            currentPlayer {
                addCardToHand(StormwindChampion)
                removeMinion(0)
            }
        }
    }
}
