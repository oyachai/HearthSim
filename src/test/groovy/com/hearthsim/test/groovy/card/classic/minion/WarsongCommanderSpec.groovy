package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.minion.WarsongCommander
import com.hearthsim.card.classic.minion.common.FaerieDragon
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.card.classic.minion.rare.KnifeJuggler
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode
import com.hearthsim.util.tree.RandomEffectNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class WarsongCommanderSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([BloodfenRaptor, WarGolem])
                field([[minion: WarsongCommander]])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "gives minions charge"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(BloodfenRaptor, 0)
                mana(8)
                numCardsUsed(1)
                updateMinion(0, [charge: true])
            }
        }
    }

    def "does not trigger on enemy play"() {
        startingBoard.removeMinion(CURRENT_PLAYER, 0);
        startingBoard.placeMinion(WAITING_PLAYER, new WarsongCommander())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
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

    def "does not trigger on big minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(1)
        def ret = theCard.useOn(CURRENT_PLAYER, 0, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(WarGolem, 0)
                mana(3)
                numCardsUsed(1)
            }
        }
    }
}
