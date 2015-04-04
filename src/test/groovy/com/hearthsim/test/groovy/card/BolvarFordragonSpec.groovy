package com.hearthsim.test.groovy.card

import com.hearthsim.card.minion.concrete.*
import com.hearthsim.card.spellcard.concrete.Fireball
import com.hearthsim.card.spellcard.concrete.Whirlwind
import com.hearthsim.model.BoardModel
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class BolvarFordragonSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Fireball, Whirlwind, BolvarFordragon])
                field([[minion: IronbeakOwl], [minion: Boar]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: BloodfenRaptor],[minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "buffs on friendly minion death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateCardInHand(2, [deltaAttack: +1])
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
                removeMinion(0)
            }
        }
    }

    def "does not trigger on enemy minion death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }
    }

    def "does not trigger while in play"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        def copiedBoard = ret.data_.deepCopy()

        theCard = root.data_.getCurrentPlayer().getHand().get(0)
        ret = theCard.useOn(CURRENT_PLAYER, 2, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(1)
                numCardsUsed(2)
                removeMinion(1)
            }
        }
    }

    def "buffs for each death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateCardInHand(2, [deltaAttack: +2])
                removeCardFromHand(Whirlwind)
                mana(9)
                numCardsUsed(1)
                removeMinion(1)
                removeMinion(0)
            }
            waitingPlayer {
                updateMinion(1, [deltaHealth: -1])
                updateMinion(0, [deltaHealth: -1])
            }
        }
    }
}
