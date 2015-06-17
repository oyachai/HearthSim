package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.Moonfire
import com.hearthsim.card.goblinsvsgnomes.minion.common.FloatingWatcher
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class FloatingWatcherSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Moonfire])
                field([[minion:FloatingWatcher]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "buffs after hero takes damage"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Moonfire)
                heroHealth(29)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 2, deltaHealth: 2, deltaMaxHealth: 2])
            }
        }
    }

    def "does not buff after hurting enemy"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Moonfire)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(29)
            }
        }
    }

    def "does not buff if opponent's turn"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.placeMinion(WAITING_PLAYER, new FloatingWatcher());

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Moonfire)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(29)
            }
        }
    }
}
