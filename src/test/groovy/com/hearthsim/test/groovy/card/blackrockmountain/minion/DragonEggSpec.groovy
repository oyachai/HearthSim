package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.HolySmite
import com.hearthsim.card.basic.spell.ShadowWordPain
import com.hearthsim.card.blackrockmountain.minion.common.BlackWhelp
import com.hearthsim.card.blackrockmountain.minion.rare.DragonEgg
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

/**
 * Created by oyachai on 8/14/15.
 */
class DragonEggSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([HolySmite, ShadowWordPain])
                field([[minion:DragonEgg]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:DragonEgg]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "summons a black whelp on damage"() {
        def copiedBoard = startingBoard.deepCopy()
        def holySmite = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = holySmite.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HolySmite)
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(BlackWhelp)
                mana(9)
                numCardsUsed(1)
            }
        }
    }

    def "summons a black whelp on damage on the opponent side"() {
        def copiedBoard = startingBoard.deepCopy()
        def holySmite = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = holySmite.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(HolySmite)
                mana(9)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(BlackWhelp)
            }
        }
    }

    def "killing without damage does not summon a black whelp"() {
        def copiedBoard = startingBoard.deepCopy()
        def holySmite = root.data_.getCurrentPlayer().getHand().get(1)

        def ret = holySmite.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(ShadowWordPain)
                removeMinion(CharacterIndex.MINION_1)
                mana(8)
                numCardsUsed(1)
            }
        }
    }
}
