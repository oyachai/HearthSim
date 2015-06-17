package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.Boar
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.Whirlwind
import com.hearthsim.card.classic.minion.common.IronbeakOwl
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.card.goblinsvsgnomes.minion.legendary.BolvarFordragon
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
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
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                updateCardInHand(2, [deltaAttack: +1])
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
                removeMinion(CharacterIndex.MINION_1)
            }
        }
    }

    def "does not trigger on enemy minion death"() {
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

    def "does not trigger while in play"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def copiedBoard = ret.data_.deepCopy()

        theCard = root.data_.getCurrentPlayer().getHand().get(0)
        ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.MINION_2, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(1)
                numCardsUsed(2)
                removeMinion(CharacterIndex.MINION_2)
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
                updateCardInHand(2, [deltaAttack: +2])
                removeCardFromHand(Whirlwind)
                mana(9)
                numCardsUsed(1)
                removeMinion(CharacterIndex.MINION_2)
                removeMinion(CharacterIndex.MINION_1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
        }
    }
}
