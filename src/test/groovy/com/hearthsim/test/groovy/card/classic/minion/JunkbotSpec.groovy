package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.MechanicalDragonling
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.Whirlwind
import com.hearthsim.card.classic.minion.common.DamagedGolem
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.card.classic.minion.epic.PatientAssassin
import com.hearthsim.card.goblinsvsgnomes.minion.epic.Junkbot
import com.hearthsim.card.goblinsvsgnomes.minion.rare.Shadowboxer
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class JunkbotSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Fireball, Whirlwind])
                field([[minion: Junkbot], [minion: DamagedGolem], [minion: MechanicalDragonling], [minion: PatientAssassin]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: Shadowboxer],[minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "buffs on friendly mech death"() {
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
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: +2, deltaMaxHealth: +2, deltaAttack: +2])
            }
        }
    }

    def "does not trigger on enemy mech death"() {
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

    def "does not trigger on non-mech death"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_4, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
                removeMinion(CharacterIndex.MINION_4)
            }
        }
    }

    def "buffs for each death"() {
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
                removeMinion(CharacterIndex.MINION_4)
                removeMinion(CharacterIndex.MINION_3)
                removeMinion(CharacterIndex.MINION_2)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: +3, deltaMaxHealth: +4, deltaAttack: +4]) // +2/+2, +2/+2 and take one damage from Whirlwind
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }
    }

    def "does not trigger while in hand"() {
        startingBoard.modelForSide(CURRENT_PLAYER).placeCardHand(new Junkbot())
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1)

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }
}
