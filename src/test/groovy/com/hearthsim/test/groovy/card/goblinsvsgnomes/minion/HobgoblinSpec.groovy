package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

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
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Wisp, 0)
                mana(10)
                numCardsUsed(1)
                updateMinion(0, [attackDelta: 2, healthDelta: 2, maxHealthDelta: 2])
            }
        }
    }

    // TODO unverified behavior
    def "only one hobgobin triggers"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new Hobgoblin())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Wisp, 0)
                mana(10)
                numCardsUsed(1)
                updateMinion(0, [attackDelta: 2, healthDelta: 2, maxHealthDelta: 2])
            }
        }
    }

    def "does not trigger on enemy play"() {
        startingBoard.removeMinion(CURRENT_PLAYER, 0);
        startingBoard.placeMinion(WAITING_PLAYER, new Hobgoblin())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(Wisp, 0)
                mana(10)
                numCardsUsed(1)
            }
        }
    }

    def "does not trigger on big minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(2)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, 0)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "does not trigger on small minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(TargetDummy, 0)
                mana(10)
                numCardsUsed(1)
            }
        }
    }
}
