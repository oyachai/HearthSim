package com.hearthsim.test.groovy.card.classic.spell

import com.hearthsim.Game
import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.BloodfenRaptor
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.classic.spell.common.Conceal
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals

/**
 * Created by oyachai on 8/10/15.
 */
class ConcealSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Conceal])
                field([[minion: WarGolem], [minion: BloodfenRaptor]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "Stealthed until next turn"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Conceal)
                mana(9)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [stealthedUntilNextTurn: true])
                updateMinion(CharacterIndex.MINION_2, [stealthedUntilNextTurn: true])
            }
        }
    }

    def "no longer stealthed when the next turn begins"() {
        def copiedBoard = startingBoard.deepCopy()

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        def ret2 = new HearthTreeNode(Game.beginTurn(ret.data_.deepCopy()))

        expect:
        assertEquals(root, ret);

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Conceal)
                mana(9)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [stealthedUntilNextTurn: true])
                updateMinion(CharacterIndex.MINION_2, [stealthedUntilNextTurn: true])
            }
        }

        assertBoardDelta(ret.data_, ret2.data_) {
            currentPlayer {
                mana(10)
                numCardsUsed(0)
                fatigueDamage(2)
                heroHealth(29) //fatigue damage
                updateMinion(CharacterIndex.MINION_1, [stealthedUntilNextTurn: false])
                updateMinion(CharacterIndex.MINION_2, [stealthedUntilNextTurn: false])
            }
        }

    }
}
