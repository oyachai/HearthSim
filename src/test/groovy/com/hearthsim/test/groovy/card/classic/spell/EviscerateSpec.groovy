package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.TheCoin
import com.hearthsim.card.classic.spell.common.Eviscerate
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.assertEquals

/**
 * Created by oyachai on 3/20/15.
 */
class EviscerateSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Eviscerate, TheCoin])
                mana(2)
            }
            waitingPlayer {
                field([[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "2 damage when used without combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Eviscerate)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth : -2])
            }
        }
    }

    def "4 damage when used with combo"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCoin = root.data_.getCurrentPlayer().getHand().get(1)
        def ret0 = theCoin.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def theCard = ret0.data_.getCurrentPlayer().getHand().get(0)
        def ret1 = theCard.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root)

        expect:
        assertEquals(root, ret1);

        assertBoardDelta(copiedBoard, ret1.data_) {
            currentPlayer {
                removeCardFromHand(TheCoin)
                removeCardFromHand(Eviscerate)
                mana(1)
                numCardsUsed(2)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [deltaHealth : -4])
            }
        }
    }

    def "Can target Hero"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Eviscerate)
                mana(0)
                numCardsUsed(1)
            }
            waitingPlayer {
                heroHealth(28)
            }
        }
    }

    def "Can target own Hero"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Eviscerate)
                mana(0)
                numCardsUsed(1)
                heroHealth(28)
            }
        }
    }

}
