package com.hearthsim.test.groovy.card.weapon

import com.hearthsim.card.minion.concrete.*
import com.hearthsim.card.minion.concrete.BombLobber
import com.hearthsim.card.weapon.concrete.Coghammer
import com.hearthsim.card.weapon.concrete.Glaivezooka
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class ShipsCannonSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([BloodsailRaider,BloodfenRaptor])
                field([[minion: ShipsCannon], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                hand([BloodsailRaider])
                field([[minion: FaerieDragon],[minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "returned node is normal for only one target"() {
        startingBoard.removeMinion(WAITING_PLAYER, 0);
        startingBoard.removeMinion(WAITING_PLAYER, 0);

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodsailRaider, 0)
                mana(8)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(28)
            }
        }
    }

    def "does not trigger on enemy play"() {
        startingBoard.removeMinion(CURRENT_PLAYER, 0);
        startingBoard.placeMinion(WAITING_PLAYER, new ShipsCannon())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodsailRaider, 0)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "does not trigger on non-pirate"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof HearthTreeNode
        !(ret instanceof RandomEffectNode)

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, 0)
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "returned node is RNG for two or more targets"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 3

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodsailRaider, 0)
                numCardsUsed(1)
                mana(8)
            }
        }
    }

    def "hits all enemies"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 3

        def copiedBoard = ret.data_.deepCopy()

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            waitingPlayer {
                heroHealth(28)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            waitingPlayer {
                removeMinion(0)
            }
        }

        HearthTreeNode child2 = ret.getChildren().get(2);
        assertBoardDelta(copiedBoard, child2.data_) {
            waitingPlayer {
                updateMinion(1, [deltaHealth: -2])
            }
        }
    }
}
