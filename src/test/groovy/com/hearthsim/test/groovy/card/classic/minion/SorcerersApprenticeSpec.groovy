package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.RiverCrocolisk
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.HolySmite
import com.hearthsim.card.classic.minion.common.SorcerersApprentice
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class SorcerersApprenticeSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([SorcerersApprentice, BloodfenRaptor, Fireball, HolySmite])
                mana(10)
            }
            waitingPlayer {
                hand([RiverCrocolisk, Fireball])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing Scorcer's Apprentice decreases spell mana cost"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        theCard = root.data_.getCurrentPlayer().getHand().get(1)
        ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, ret)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(SorcerersApprentice, CharacterIndex.HERO)
                mana(5)
                removeCardFromHand(Fireball)
                numCardsUsed(2)
            }
            waitingPlayer {
                heroHealth(24)
            }
        }
    }

    def "playing a spell with enemy Scorcer's Apprentice on board costs the same"() {
        startingBoard.placeMinion(WAITING_PLAYER, new SorcerersApprentice())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(6)
                removeCardFromHand(Fireball)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(24)
            }
        }
    }

    def "two Scorcer's Apprentice stack"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new SorcerersApprentice())
        startingBoard.placeMinion(CURRENT_PLAYER, new SorcerersApprentice())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(8)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(24)
            }
        }
    }

    def "cannot reduce mana cost below 0"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new SorcerersApprentice())
        startingBoard.placeMinion(CURRENT_PLAYER, new SorcerersApprentice())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(3)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HolySmite)
                mana(10)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(28)
            }
        }
    }
}
