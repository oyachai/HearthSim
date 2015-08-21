package com.hearthsim.test.groovy.card.blackrockmountain.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.spell.Fireball
import com.hearthsim.card.basic.spell.ShadowWordPain
import com.hearthsim.card.blackrockmountain.minion.common.Imp
import com.hearthsim.card.blackrockmountain.minion.common.ImpGangBoss
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER


class ImpGangBossSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Fireball, ShadowWordPain])
                field([[minion:ImpGangBoss]])
                mana(10)
            }
            waitingPlayer {
                field([[minion:ImpGangBoss]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "summons an imp on damage"() {
        def copiedBoard = startingBoard.deepCopy()
        def holySmite = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = holySmite.useOn(CURRENT_PLAYER, CharacterIndex.MINION_1, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(Imp)
                mana(6)
                numCardsUsed(1)
            }
        }
    }

    def "summons an imp on damage on the opponent side"() {
        def copiedBoard = startingBoard.deepCopy()
        def holySmite = root.data_.getCurrentPlayer().getHand().get(0)

        def ret = holySmite.useOn(WAITING_PLAYER, CharacterIndex.MINION_1, root);

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(Fireball)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                removeMinion(CharacterIndex.MINION_1)
                addMinionToField(Imp)
            }
        }
    }

    def "killing without damage does not summon an imp"() {
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
