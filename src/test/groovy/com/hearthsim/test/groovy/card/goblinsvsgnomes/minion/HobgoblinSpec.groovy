package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.classic.minion.common.Wisp
import com.hearthsim.card.goblinsvsgnomes.minion.epic.Hobgoblin
import com.hearthsim.card.goblinsvsgnomes.minion.rare.TargetDummy
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class HobgoblinSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([TargetDummy, Wisp, BloodfenRaptor])
                field([[minion: Hobgoblin]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "buffs minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Wisp, CharacterIndex.HERO)
                mana(10)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 2, deltaHealth: 2, deltaMaxHealth: 2])
            }
        }
    }

    // TODO unverified behavior (apparently both should trigger)
    def "only one hobgobin triggers"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new Hobgoblin())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Wisp, CharacterIndex.HERO)
                mana(10)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaAttack: 2, deltaHealth: 2, deltaMaxHealth: 2])
            }
        }
    }

    def "does not trigger on enemy play"() {
        startingBoard.removeMinion(CURRENT_PLAYER, CharacterIndex.MINION_1);
        startingBoard.placeMinion(WAITING_PLAYER, new Hobgoblin())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Wisp, CharacterIndex.HERO)
                mana(10)
                numCardsUsed(1)
            }
        }
    }

    def "does not trigger on big minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, CharacterIndex.HERO)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "does not trigger on small minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(TargetDummy, CharacterIndex.HERO)
                mana(10)
                numCardsUsed(1)
            }
        }
    }
}
