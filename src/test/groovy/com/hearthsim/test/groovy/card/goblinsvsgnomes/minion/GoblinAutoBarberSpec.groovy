package com.hearthsim.test.groovy.card.goblinsvsgnomes.minion

import com.hearthsim.card.CharacterIndex
import com.hearthsim.card.basic.weapon.FieryWarAxe
import com.hearthsim.card.goblinsvsgnomes.minion.common.GoblinAutoBarber
import com.hearthsim.model.BoardModel
import com.hearthsim.test.groovy.card.CardSpec
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER

class GoblinAutoBarberSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {

        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([GoblinAutoBarber])
                weapon(FieryWarAxe) {
                    weaponCharge(2)
                }
                mana(10)
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "battlecry buffs weapon"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(CURRENT_PLAYER, CharacterIndex.HERO, root)

        expect:
        ret != null

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                playMinion(GoblinAutoBarber)
                mana(8)
                numCardsUsed(1)
                weaponDamage(4)
            }
        }
    }
}
