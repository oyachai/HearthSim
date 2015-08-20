package com.hearthsim.test.groovy.card.blackrockmountain.spell

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.WarGolem
import com.hearthsim.card.basic.spell.HolySmite
import com.hearthsim.card.blackrockmountain.minion.common.AxeFlinger
import com.hearthsim.card.blackrockmountain.spell.rare.Revenge
import com.hearthsim.card.classic.minion.common.AbusiveSergeant
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static org.junit.Assert.assertEquals

/**
 * Created by oyachai on 8/20/15.
 */
class RevengeSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Revenge])
                field([[minion:AxeFlinger]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:AxeFlinger], [minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "deals one damage when full health"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Revenge)
                heroHealth(28)
                mana(8)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
            }
            waitingPlayer {
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -1])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -1])
            }
        }
    }

    def "deals three damage when at 10 health"() {
        startingBoard.getCurrentPlayer().getHero().setHealth((byte)10)
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Revenge)
                heroHealth(8)
                mana(8)
                numCardsUsed(1)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -3])
            }
            waitingPlayer {
                heroHealth(28)
                updateMinion(CharacterIndex.MINION_1, [deltaHealth: -3])
                updateMinion(CharacterIndex.MINION_2, [deltaHealth: -3])
            }
        }
    }

}
