package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.basic.minion.ChillwindYeti
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.ShadowWordDeath
import com.hearthsim.card.classic.minion.common.StranglethornTiger
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

class ShadowWordDeathSpec extends CardSpec {
    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([ShadowWordDeath])
                mana(5)
            }
            waitingPlayer {
                field([[minion: WarGolem], [minion: ChillwindYeti], [minion: StranglethornTiger]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "cannot target low attack minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 2, root, null, null)

        expect:
        assertNull(ret);
        assertBoardEquals(copiedBoard, root.data_);
    }

    def "kills high attack minion"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 1, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, root.data_) {
            currentPlayer {
                removeCardFromHand(ShadowWordDeath)
                mana(2)
                numCardsUsed(1)
            }
            waitingPlayer { removeMinion(0) }
        }
    }

    def "can target minion with extra attack"() {
        def copiedBoard = startingBoard.deepCopy()

        def target = root.data_.modelForSide(WAITING_PLAYER).getCharacter(1)
        target.extraAttackUntilTurnEnd += 2

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, target, root, null, null)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, root.data_) {
            currentPlayer {
                removeCardFromHand(ShadowWordDeath)
                mana(2)
                numCardsUsed(1)
            }
            waitingPlayer { removeMinion(0) }
        }
    }

    def "follows normal targeting rules"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 3, root, null, null)

        expect:
        assertNull(ret);

        assertBoardEquals(copiedBoard, root.data_)
    }
}
