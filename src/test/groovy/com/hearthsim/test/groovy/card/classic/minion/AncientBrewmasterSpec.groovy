package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.StormwindChampion
import com.hearthsim.card.classic.minion.common.AncientBrewmaster
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null)
        assertEquals(ret.numChildren(), 0)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientBrewmaster)
                mana(3)
                numCardsUsed(1)
                addCardToHand(StormwindChampion)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

    def "bounced minion does not remember state"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new StormwindChampion())
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).health = 2
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).attack = 2
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).divineShield = true
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).windfury = true
        startingBoard.modelForSide(CURRENT_PLAYER).getCharacter(CharacterIndex.MINION_1).spellDamage = 4

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertFalse(ret == null)
        assertEquals(ret.numChildren(), 0)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(AncientBrewmaster)
                mana(3)
                numCardsUsed(1)
                addCardToHand(StormwindChampion)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
}
