package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.minion.legendary.LorewalkerCho
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.CardDrawNode
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class LorewalkerChoSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Fireball, BloodfenRaptor])
                field([[minion: LorewalkerCho]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "gives opponent copy after spell played"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        !(ret instanceof CardDrawNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(24)
                addCardToHand(Fireball)
            }
        }
    }

    def "does not copy card after minion played"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null
        !(ret instanceof CardDrawNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "copies after opponent spell"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1)
        startingBoard.placeMinion(WAITING_PLAYER, new LorewalkerCho())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        !(ret instanceof CardDrawNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(24)
                addCardToHand(Fireball)
            }
        }
    }

    def "fizzles if hand is full"() {
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())
        startingBoard.placeCardHand(WAITING_PLAYER, new TheCoin())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        !(ret instanceof CardDrawNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(24)
            }
        }
    }
}
