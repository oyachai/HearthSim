package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.RiverCrocolisk
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.HolySmite
import com.hearthsim.card.classic.minion.rare.ManaWraith
import com.hearthsim.card.classic.spell.common.Silence
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class ManaWraithSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ManaWraith, BloodfenRaptor, HolySmite, Silence])
                mana(10)
            }
            waitingPlayer {
                hand([RiverCrocolisk, Fireball])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "playing mana wraith increases minion mana cost"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(ManaWraith, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "killing a mana wraith returns minion mana cost back to normal"() {
        startingBoard.placeMinion(WAITING_PLAYER, new ManaWraith())

        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        def copiedBoard = ret.data_.deepCopy()
        theCard = root.data_.getCurrentPlayer().getHand().get(1)
        ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(2)
            }
        }
    }

    def "silencing a mana wraith returns minion mana cost back to normal"() {
        startingBoard.placeMinion(WAITING_PLAYER, new ManaWraith())

        def theCard = root.data_.getCurrentPlayer().getHand().get(3)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        def copiedBoard = ret.data_.deepCopy()
        theCard = root.data_.getCurrentPlayer().getHand().get(1)
        ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(2)
            }
        }
    }

    // This test also makes sure the mana effect actually sticks since our test helpers use placeMinion directly
    def "playing a minion with enemy Mana Wraith on board costs 1 more"() {
        startingBoard.placeMinion(WAITING_PLAYER, new ManaWraith())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, CharacterIndex.HERO)
                mana(7)
                numCardsUsed(1)
            }
        }
    }

    def "two mana wraiths stack"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new ManaWraith())
        startingBoard.placeMinion(WAITING_PLAYER, new ManaWraith())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null;

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, CharacterIndex.HERO)
                mana(6)
                numCardsUsed(1)
            }
        }
    }
}
