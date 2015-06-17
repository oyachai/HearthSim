package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.Boar
import com.hearthsim.card.basic.minion.MurlocRaider
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.Whirlwind
import com.hearthsim.card.classic.minion.common.IronbeakOwl
import com.hearthsim.card.classic.minion.common.LeperGnome
import com.hearthsim.card.classic.spell.epic.TwistingNether
import com.hearthsim.card.goblinsvsgnomes.minion.legendary.MekgineerThermaplugg
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class MekgineerThermapluggSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Fireball, Whirlwind, TwistingNether])
                field([[minion: MekgineerThermaplugg], [minion: IronbeakOwl]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: Boar],[minion: MurlocRaider]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "summons leper gnome on enemy minion death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
                addMinionToField(LeperGnome, CharacterIndex.MINION_1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

    def "does not trigger on friendly minion death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
                removeMinion(CharacterIndex.MINION_2)
            }
        }
    }

    def "triggers for each death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Whirlwind)
                mana(9)
                numCardsUsed(1)
                removeMinion(CharacterIndex.MINION_2)
                addMinionToField(LeperGnome, CharacterIndex.MINION_1)
                addMinionToField(LeperGnome, CharacterIndex.MINION_1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

    // TODO unverified behavior
    def "does not trigger if also died"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(TwistingNether)
                mana(2)
                numCardsUsed(1)
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

    def "does not trigger while in hand"() {
        startingBoard.modelForSide(CURRENT_PLAYER).placeCardHand(new MekgineerThermaplugg())
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
}
