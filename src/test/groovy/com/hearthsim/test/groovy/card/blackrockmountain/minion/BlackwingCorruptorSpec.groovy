package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.blackrockmountain.minion.common.AxeFlinger
import com.hearthsim.card.blackrockmountain.minion.common.BlackwingCorruptor
import com.hearthsim.card.classic.minion.common.AbusiveSergeant
import com.hearthsim.card.classic.minion.rare.AzureDrake
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals

class BlackwingCorruptorSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([BlackwingCorruptor])
                field([[minion:AxeFlinger]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:AxeFlinger], [minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }


    def "no damage when not holding a dragon"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new BlackwingCorruptor()
        def ret = theCard.battlecryEffect.applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:

        assertBoardDelta(copiedBoard, ret.data_) {
        }
    }

    def "does 3 damage when holding a dragon"() {
        startingBoard.placeCardHand(CURRENT_PLAYER, new AzureDrake())
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new BlackwingCorruptor()
        def ret = theCard.battlecryEffect.applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -3])
            }
            waitingPlayer {
                heroHealth(28)
            }
        }
    }

    def "doesn't matter if the opponent is holding a dragon"() {
        startingBoard.placeCardHand(WAITING_PLAYER, new AzureDrake())
        def copiedBoard = startingBoard.deepCopy()
        def theCard = new BlackwingCorruptor()
        def ret = theCard.battlecryEffect.applyEffect(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:

        assertBoardDelta(copiedBoard, ret.data_) {
        }
    }
}
