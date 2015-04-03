package com.hearthsim.test.groovy.card.spell

import com.hearthsim.card.minion.concrete.*
import com.hearthsim.card.minion.concrete.BombLobber
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class BombLobberSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([BombLobber])
                field([[minion: Voidwalker], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: FaerieDragon],[minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "returned node is normal for only one target"() {
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
                playMinion(BombLobber, 0)
                mana(5)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(0, [deltaHealth: -4])
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
        ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                numCardsUsed(1)
            }
        }
    }

    def "returned node is normal for no targets"() {
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
                playMinion(BombLobber, 0)
                mana(5)
                numCardsUsed(1)
            }
        }
    }

    def "hits enemy minions"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 2

        def copiedBoard = ret.data_.deepCopy()

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinion(BombLobber, 0)
                mana(5)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                playMinion(BombLobber, 0)
                mana(5)
            }
            waitingPlayer {
                updateMinion(1, [deltaHealth: -4])
            }
        }
    }

    def "is not effected by spellpower"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new KoboldGeomancer())

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 2

        def copiedBoard = ret.data_.deepCopy()

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                playMinion(BombLobber, 0)
                mana(5)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                playMinion(BombLobber, 0)
                mana(5)
            }
            waitingPlayer {
                updateMinion(1, [deltaHealth: -4])
            }
        }
    }
}
