package com.hearthsim.test.groovy.card.classic.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.minion.SenjinShieldmasta
import com.hearthsim.card.classic.minion.common.ScarletCrusader
import com.hearthsim.card.classic.spell.rare.MassDispel
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.CardDrawNode
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER

class MassDispelSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([MassDispel])
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "silences enemy minions"() {
        startingBoard.placeMinion(WAITING_PLAYER, new SenjinShieldmasta())
        startingBoard.placeMinion(WAITING_PLAYER, new ScarletCrusader())

        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof CardDrawNode
        ((CardDrawNode)ret).numCardsToDraw == 1

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(MassDispel)
                mana(6)
                numCardsUsed(1)
            }
            waitingPlayer {
                updateMinion(CharacterIndex.MINION_1, [silenced:true, taunt:false])
                updateMinion(CharacterIndex.MINION_2, [silenced:true, divineShield:false])
            }
        }
    }

    def "ignores own minions"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null
        ret instanceof CardDrawNode
        ((CardDrawNode)ret).numCardsToDraw == 1

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                removeCardFromHand(MassDispel)
                mana(6)
                numCardsUsed(1)
            }
        }
    }
}
